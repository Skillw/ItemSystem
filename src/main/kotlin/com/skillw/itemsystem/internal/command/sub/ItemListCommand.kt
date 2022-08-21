package com.skillw.itemsystem.internal.command.sub

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.internal.feature.ItemSyncer.syncNBT
import com.skillw.itemsystem.internal.manager.ISConfig
import com.skillw.pouvoir.util.StringUtils.replacement
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.common.util.isConsole
import taboolib.common.util.sync
import taboolib.common5.Coerce
import taboolib.module.chat.TellrawJson
import taboolib.module.chat.colored
import taboolib.module.nms.getName
import taboolib.platform.util.hoverItem

object ItemListCommand {
    val list = subCommand {
        val section = ISConfig.listSection ?: return@subCommand
        fun get(path: String): String {
            return section.get(path).toString()
        }
        dynamic(optional = true) {
            restrict<ProxyCommandSender> { _, _, argument ->
                Coerce.asInteger(argument).isPresent
            }
            execute<ProxyCommandSender> { sender, _, argument ->
                submit(async = true) {
                    val items = ItemSystem.itemBuilderManager.values.toList()

                    val page = argument.toIntOrNull() ?: 1

                    val size = get("pre-page-size").toIntOrNull() ?: 10
                    val total = ItemSystem.itemBuilderManager.size
                    val lastPage = total / size + if (total % size != 0) 1 else 0

                    val up = get("up").colored()
                    val format = get("format").colored()
                    val leftSymbol = get("left").colored()
                    val rightSymbol = get("right").colored()
                    val down = get("down").colored()

                    submit { sender.sendMessage(up) }
                    var raw: TellrawJson
                    val beginIndex = (page - 1) * size
                    val lastIndex = if (page != lastPage) {
                        size * page
                    } else {
                        total
                    } - 1
                    for (index in beginIndex..lastIndex) {
                        val item = items[index]
                        val player = if (!sender.isConsole()) sender.cast<Player>() else null
                        val itemStack = item.build(player)
                        player?.let { itemStack.syncNBT(it) }
                        raw = TellrawJson()
                            .append(
                                format.replacement(
                                    mapOf(
                                        "{order}" to "${index + 1}",
                                        "{key}" to item.key,
                                        "{name}" to itemStack.getName().colored()
                                    )
                                )
                            )
                            .hoverItem(itemStack)
                        if (sender is ProxyPlayer) {
                            raw.runCommand("/is get ${item.key}")
                        }
                        sync { raw.sendTo(sender) }
                    }
                    raw = TellrawJson()
                    val previousPage = page - 1
                    val left = TellrawJson().append(leftSymbol)
                    if (previousPage > 0) {
                        left.runCommand("/is list $previousPage")
                    }

                    val nextPage = page - 1
                    val right = TellrawJson().append(rightSymbol)
                    if (nextPage > 0) {
                        right.runCommand("/is list $nextPage")
                    }

                    val pageInfo =
                        get("page-info").replacement(mapOf("{current}" to "$page", "{total}" to "$lastPage")).colored()

                    submit {
                        raw.append(left)
                            .append(pageInfo)
                            .append(right)
                            .sendTo(sender)
                        sender.sendMessage(down.colored())
                    }
                }
            }
        }
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.performCommand("is list 1")
        }
    }
}