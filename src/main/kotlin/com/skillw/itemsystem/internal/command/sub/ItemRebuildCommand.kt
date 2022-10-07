package com.skillw.itemsystem.internal.command.sub

import com.skillw.itemsystem.api.ItemAPI.replace
import com.skillw.itemsystem.internal.feature.ItemUpdater.updateItem
import com.skillw.itemsystem.util.GsonUtils.parseToMap
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submitAsync
import taboolib.common.util.sync
import taboolib.common5.Demand
import taboolib.module.chat.TellrawJson
import taboolib.module.lang.asLangText
import taboolib.module.lang.sendLang
import taboolib.module.nms.getItemTag
import taboolib.module.nms.getName
import taboolib.platform.util.hoverItem
import taboolib.platform.util.isAir
import taboolib.platform.util.onlinePlayers

object ItemRebuildCommand {
    val rebuild = subCommand {
        dynamic(optional = true) {
            suggestion<ProxyCommandSender> { sender, context ->
                onlinePlayers.map { it.name }
            }
            dynamic {
                suggestion<ProxyCommandSender>(uncheck = true) { sender, context ->
                    listOf("-vars all -data {}")
                }
                execute<ProxyCommandSender> { sender, context, argument ->
                    val name = context.argument(-1)
                    val player = Bukkit.getPlayer(name)
                    if (player == null) {
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
        val oldItem = old.clone().apply { replace(player) }
        val newItem = new.clone().apply { replace(player) }
        TellrawJson()
            .append(
                TellrawJson()
                    .append(asLangText("command-rebuild-item-old", oldItem.getName()))
                    .hoverItem(oldItem)
            )
            .append(
                TellrawJson()
                    .append(
                        asLangText("command-rebuild-item-new", newItem.getName())
                    )
                    .hoverItem(newItem)
            )
            .append(
                TellrawJson()
                    .append(asLangText("command-rebuild-item-info", variables.toString(), data.toString()))
                    .hoverText(data.map { (key, value) -> "$key = $value" }.joinToString { "\n" })
            )
            .sendTo(this)
    }
}