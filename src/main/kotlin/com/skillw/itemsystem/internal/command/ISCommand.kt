package com.skillw.itemsystem.internal.command

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.internal.command.sub.*
import org.bukkit.command.CommandSender
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang


@CommandHeader(name = "item", aliases = ["is"])
object ISCommand {

    @CommandBody
    val help = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("command-info")
        }
    }

    @CommandBody
    val main = mainCommand {
        incorrectSender { sender, _ ->
            sender.sendLang("command-only-player")
        }
        incorrectCommand { sender, _, _, _ ->
            sender.sendLang("command-valid-command")
        }

        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("command-info")
        }

    }

    @CommandBody(permission = "is.command.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            ItemSystem.reload()
            sender.sendLang("command-reload")
        }
    }

    @CommandBody(permission = "is.command.report")
    val report = MirrorCommand.report

    @CommandBody(permission = "is.command.clear")
    val clear = MirrorCommand.clear

    @CommandBody(permission = "is.command.get")
    val get = ItemProductCommand.get

    @CommandBody(permission = "is.command.give")
    val give = ItemProductCommand.give

    @CommandBody(permission = "is.command.drop")
    val drop = ItemProductCommand.drop

    @CommandBody(permission = "is.command.list")
    val list = ItemListCommand.list

    @CommandBody(permission = "is.command.save")
    val save = ItemSaveCommand.save

    @CommandBody(permission = "is.command.rebuild")
    val rebuild = ItemRebuildCommand.rebuild

}