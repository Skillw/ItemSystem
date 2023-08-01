package com.skillw.itemsystem.internal.command.sub

import com.skillw.itemsystem.internal.command.ISCommand.soundClick
import com.skillw.itemsystem.internal.command.ISCommand.soundFail
import com.skillw.itemsystem.internal.feature.ItemUpdater.updateItem
import com.skillw.itemsystem.util.GsonUtils.parseToMap
import com.skillw.itemsystem.util.ItemUtils.displayClone
import com.skillw.itemsystem.util.ItemUtils.displayItem
import com.skillw.pouvoir.util.soundFinish
import com.skillw.pouvoir.util.soundSuccess
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import taboolib.common.util.sync
import taboolib.common5.Demand
import taboolib.module.chat.TellrawJson
import taboolib.module.lang.asLangText
import taboolib.module.lang.sendLang
import taboolib.module.nms.getItemTag
import taboolib.module.nms.getName
import taboolib.platform.util.isAir
import taboolib.platform.util.onlinePlayers

object ItemRebuildCommand {
    val rebuild = subCommand {
        dynamic(optional = true) {
            suggestion<ProxyCommandSender> { sender, context ->
                sender.soundClick()
                onlinePlayers.map { it.name }
            }
            dynamic {
                suggestion<ProxyCommandSender>(uncheck = true) { sender, context ->
                    sender.soundClick()
                    listOf("-vars all -data {}")
                }
                execute<ProxyCommandSender> { sender, context, argument ->
                    val name = context.argument(-1)
                    val player = Bukkit.getPlayer(name)
                    if (player == null) {
                        sender.soundFail()
                        sender.sendLang("command-valid-player", name)
                        return@execute
                    }
                    rebuildItemInHand(player, sender, argument)
                }
            }
        }
        execute<ProxyPlayer> { sender, _, _ ->
            rebuildItemInHand(sender.cast(), sender)
        }
    }

    private fun rebuildItemInHand(
        player: Player,
        sender: ProxyCommandSender = console(),
        argument: String = "",
    ) {
        val itemInHand: ItemStack = player.equipment!!.itemInMainHand
        if (itemInHand.isAir()) {
            sender.sendLang("command-valid-is-item-in-hand")
            return
        }
        val itemTag = itemInHand.getItemTag()
        if (!itemTag.containsKey("ITEM_SYSTEM")) {
            sender.sendLang("command-valid-is-item-in-hand")
            return
        }
        submitAsync {
            val demand = Demand("ad $argument")
            val variables = demand.get("vars", "all").toString().filter { it != ' ' }
                .run {
                    if (contains(",")) {
                        split(",").toSet()
                    } else {
                        setOf(this)
                    }
                }
            val data = demand.get("data", "{}")!!.parseToMap()

            val newItem = itemInHand.updateItem(player, variables, data)

            sync { player.inventory.setItemInMainHand(newItem) }
            adaptPlayer(player).sendRebuildMessage(itemInHand, newItem, variables, data)
        }
    }

    @Suppress("DEPRECATION")
    private fun ProxyPlayer.sendRebuildMessage(
        old: ItemStack,
        new: ItemStack,
        variables: Set<String>,
        data: Map<String, Any>,
    ) {
        val player = cast<Player>()
        player.soundSuccess()
        submit(delay = 10) {
            player.soundFinish()
        }
        val oldItem = old.displayClone(player)
        val newItem = new.displayClone(player)
        TellrawJson()
            .append(
                TellrawJson()
                    .append(asLangText("command-rebuild-item-old", oldItem.getName()))
            )
            .append(
                TellrawJson()
                    .append(console().asLangText("show-item"))
                    .displayItem(oldItem)
            )
            .append(
                TellrawJson()
                    .append(
                        asLangText("command-rebuild-item-new", newItem.getName())
                    )
            )
            .append(
                TellrawJson()
                    .append(console().asLangText("show-item"))
                    .displayItem(newItem)
            )
            .append(
                TellrawJson()
                    .append(asLangText("command-rebuild-item-info", variables.toString(), data.toString()))
                    .hoverText(data.map { (key, value) -> "$key = $value" }.joinToString { "\n" })
            )
            .sendTo(this)
    }
}
