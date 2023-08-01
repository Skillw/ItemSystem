package com.skillw.itemsystem.internal.command.sub

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.internal.core.builder.ItemBuilder
import com.skillw.itemsystem.util.ItemUtils.displayClone
import com.skillw.itemsystem.util.ItemUtils.displayItem
import com.skillw.pouvoir.util.soundClick
import com.skillw.pouvoir.util.soundFail
import com.skillw.pouvoir.util.soundSuccess
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.io.newFile
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.module.chat.TellrawJson
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.module.lang.asLangText
import taboolib.module.nms.getName

import taboolib.platform.util.isAir
import taboolib.platform.util.sendLang

object ItemSaveCommand {
    val save = subCommand {
        dynamic {
            suggestion<Player>(uncheck = true) { sender, _ ->
                sender.soundClick()
                ItemSystem.itemBuilderManager.files.map { it.name }
            }
            restrict<Player> { _, _, argument ->
                argument.endsWith(".yml")
            }
            dynamic {
                suggestion<Player>(uncheck = true) { sender, _ ->
                    sender.soundClick()
                    listOf("物品id")
                }
                execute<Player> { sender, context, key ->
                    val itemInHand = sender.inventory.itemInMainHand
                    if (itemInHand.isAir()) {
                        sender.sendLang("command-valid-item-in-hand")
                        sender.soundFail()
                        return@execute
                    }
                    if (ItemSystem.itemBuilderManager.containsKey(key)) {
                        sender.soundFail()
                        sender.sendLang("command-id-exists", key)
                        return@execute
                    }
                    val fileName = context.argument(-1)
                    submit(async = true) {
                        val map = ItemBuilder.serialize(itemInHand)
                        val file = newFile(ItemSystem.plugin.dataFolder, "items/${fileName}")
                        val origin = file.readText()
                        file.delete()
                        file.createNewFile()
                        val config = Configuration.loadFromFile(file, Type.YAML)
                        config[key] = map
                        val str = config.saveToString()
                        file.writeText(origin + str)
                        ItemSystem.reload()
                    }
                    adaptPlayer(sender).sendSaveMessage(sender, itemInHand, key, fileName)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun ProxyCommandSender.sendSaveMessage(
        player: Player,
        itemStack: ItemStack,
        key: String,
        fileName: String,
    ) {
        player.soundSuccess()
        val item = itemStack.displayClone(player)
        TellrawJson()
            .append(
                TellrawJson()
                    .append(asLangText("command-save-item-item", item.getName()))
            )
            .append(
                TellrawJson()
                    .append(console().asLangText("click-to-get-item"))
                    .displayItem(itemStack)
                    .runCommand("/item get $key")
            )
            .append(
                TellrawJson()
                    .append(
                        asLangText("command-save-item-key-file", key, fileName)
                    )
            )
            .sendTo(this)
    }
}
