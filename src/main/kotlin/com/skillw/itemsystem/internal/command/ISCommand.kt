package com.skillw.itemsystem.internal.command

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.internal.command.sub.*
import com.skillw.pouvoir.util.PlayerUtils.soundClick
import com.skillw.pouvoir.util.PlayerUtils.soundFail
import com.skillw.pouvoir.util.PlayerUtils.soundSuccess
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang


@CommandHeader(name = "item")
object ISCommand {
    internal fun ProxyCommandSender.soundSuccess() {
        (this.origin as? Player?)?.soundSuccess()
    }

    internal fun ProxyCommandSender.soundFail() {
        (this.origin as? Player?)?.soundFail()
    }

    internal fun ProxyCommandSender.soundClick() {
        (this.origin as? Player?)?.soundClick()
    }

    @CommandBody
    val help = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("command-info")
        }
    }

    @CommandBody
    val main = mainCommand {
        incorrectSender { sender, _ ->
            sender.soundFail()
            sender.sendLang("command-only-player")
        }
        incorrectCommand { sender, _, _, _ ->
            sender.soundFail()
            sender.sendLang("command-valid-command")
        }

        execute<ProxyCommandSender> { sender, _, _ ->
            sender.soundSuccess()
            sender.sendLang("command-info")
        }

    }

    @CommandBody(permission = "is.command.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            (sender as? Player)?.soundSuccess()
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