package com.skillw.itemsystem.internal.command.sub

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.internal.builder.ItemBuilder
import org.bukkit.entity.Player
import taboolib.common.io.newFile
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.platform.util.isAir
import taboolib.platform.util.sendLang
import java.io.FileWriter

object ItemSaveCommand {
    val save = subCommand {
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                ItemSystem.itemBuilderManager.files.map { it.name }
            }
            restrict<Player> { _, _, argument ->
                argument.endsWith(".yml")
            }
            dynamic {
                suggestion<Player>(uncheck = true) { _, _ ->
                    listOf("物品id")
                }
                execute<Player> { sender, context, argument ->
                    val itemInHand = sender.inventory.itemInMainHand
                    if (itemInHand.isAir()) {
                        sender.sendLang("command-valid-item-in-hand")
                        return@execute
                    }
                    val key = argument
                    if (ItemSystem.itemBuilderManager.containsKey(key)) {
                        sender.sendLang("command-id-exists", key)
                        return@execute
                    }
                    val map = ItemBuilder.serialize(itemInHand)
                    val fileName = context.argument(-1)
                    submit(async = true) {
                        val file = newFile(ItemSystem.plugin.dataFolder, "items/${fileName}")
                        val config = Configuration.loadFromFile(file, Type.YAML)
                        config[key] = map
                        val str = config.saveToString()
                        FileWriter(file).apply {
                            write("")
                            flush()
                            close()
                        }
                        file.writeText(str)
                        ItemSystem.reload()
                    }
                    sender.sendLang("command-save-item", key, fileName)
                }
            }
        }
    }
}