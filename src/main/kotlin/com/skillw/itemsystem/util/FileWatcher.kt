package com.skillw.itemsystem.util

import taboolib.common.platform.function.console
import taboolib.common5.FileWatcher
import taboolib.module.lang.sendLang
import java.io.File

object FileWatcher {
    private val fileWatcher = FileWatcher()


    fun File.watch(callback: (File) -> Unit) {
        if (!fileWatcher.hasListener(this))
            fileWatcher.addSimpleListener(this) {
                console().sendLang("file-update", name)
                callback(this)
            }
    }

    fun File.unwatch() {
        fileWatcher.removeListener(this)
    }
}