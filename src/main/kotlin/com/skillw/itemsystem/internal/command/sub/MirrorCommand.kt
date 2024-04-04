package com.skillw.itemsystem.internal.command.sub

import com.skillw.itemsystem.internal.command.ISCommand.soundSuccess
import com.skillw.pouvoir.util.legacy.Mirror
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

object MirrorCommand {
    val report = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.soundSuccess()
            Mirror.report(sender)
        }
    }

    val clear = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("command-clear")
            sender.soundSuccess()
            Mirror.mirrorData.clear()
        }
    }
}