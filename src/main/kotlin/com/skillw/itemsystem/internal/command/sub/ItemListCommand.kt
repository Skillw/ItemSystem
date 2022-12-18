package com.skillw.itemsystem.internal.command.sub

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.internal.command.ISCommand.soundClick
import com.skillw.itemsystem.internal.core.option.OptionType
import com.skillw.itemsystem.internal.core.option.OptionType.type
import com.skillw.itemsystem.internal.manager.ISConfig
import com.skillw.itemsystem.util.ItemUtils.displayClone
import com.skillw.itemsystem.util.ItemUtils.displayItem
import com.skillw.pouvoir.util.StringUtils.replacement
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.common.util.isConsole
import taboolib.common.util.sync
import taboolib.common5.Coerce
import taboolib.module.chat.TellrawJson
import taboolib.module.chat.colored
import taboolib.module.lang.asLangText
import taboolib.module.lang.sendLang
import taboolib.module.nms.getName

object ItemListCommand {

    val list = subCommand {
        val section = ISConfig.listSection ?: return@subCommand
        fun get(path: String): String {
            return section.get(path).toString()
        }
        dynamic(optional = true) {
            suggestion<ProxyCommandSender> { sender, context ->
                sender.soundClick()
                listOf("all", *OptionType.types.toTypedArray())
            }
            dynamic(optional = true) {
                restrict<ProxyCommandSender> { _, _, argument ->
                    Coerce.asInteger(argument).isPresent
                }
                execute<ProxyCommandSender> { sender, context, argument ->
                    submit(async = true) {
                        sender.soundClick()
                        val type = context.argument(-1)
                        val items = ItemSystem.itemBuilderManager.values.toList().let {
                            if (type == "all") return@let it
                            it.filter { builder -> builder.type == type }
                        }
                        val page = argument.toIntOrNull() ?: 1

                        val size = get("pre-page-size").toIntOrNull() ?: 10
                        val total = ItemSystem.itemBuilderManager.size
                        val lastPage = total / size + if (total % size != 0) 1 else 0

                        val up = get("up").colored()
                        val format = get("format").colored()
                        val leftSymbol = get("left").colored()
                        val rightSymbol = get("right").colored()
                        val down = get("down").colored()
                        var raw: TellrawJson
                        val beginIndex = (page - 1) * size
                        val lastIndex = if (page != lastPage) {
                            size * page
                        } else {
                            total
                        } - 1
                        if (beginIndex >= total) {
                            sender.sendLang("command-list-no-more-page")
                            return@submit
                        }
                        submit { sender.sendMessage(up) }
                        for (index in beginIndex..lastIndex) {
                            val item = items.getOrNull(index) ?: break
                            val player = if (!sender.isConsole()) sender.cast<Player>() else null
                            val itemStack =
                                runCatching {
                                    item.build(player).displayClone(player)
                                }.run { exceptionOrNull()?.printStackTrace(); getOrNull() }
                            raw = TellrawJson()
                                .append(
                                    format.replacement(
                                        mapOf(
                                            "{order}" to "${index + 1}",
                                            "{key}" to item.key,
                                            "{name}" to (itemStack?.getName()?.colored()
                                                ?: console().asLangText("command-list-error-item"))

                                        )
                                    )
                                )
                                .append(
                                    TellrawJson()
                                        .append(console().asLangText("click-to-get-item"))
                                        .displayItem(itemStack)
                                        .apply {
                                            if (sender is ProxyPlayer) {
                                                runCommand("/item get ${item.key}")
                                            }
                                        }
                                )
                            sync { raw.sendTo(sender) }
                        }
                        raw = TellrawJson()
                        val previousPage = page - 1
                        val left = TellrawJson()
                            .append(leftSymbol)
                            .hoverText(sender.asLangText("command-list-previous"))
                        if (previousPage > 0) {
                            left.runCommand("/item list $type $previousPage")

                        }

                        val nextPage = page + 1
                        val right = TellrawJson()
                            .append(rightSymbol)
                            .hoverText(sender.asLangText("command-list-next"))
                        if (nextPage <= lastPage) {
                            right.runCommand("/item list $type $nextPage")
                        }

                        val pageInfo =
                            get("page-info").replacement(mapOf("{current}" to "$page", "{total}" to "$lastPage"))
                                .colored()

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
        }
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.performCommand("item list all 1")
        }
    }
}