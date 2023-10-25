package com.skillw.itemsystem.internal.feature.compat.asahi.prefix

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.itemsystem.internal.core.builder.ProcessData

@AsahiPrefix(["save"], "item_system")
private fun save() = prefixParser {
    result {
        if (this !is ProcessData) return@result "Error Context"
        if (peek() == "[") {
            return@result questList().get().onEach { savingKeys.add(it.toString()) }
        }
        return@result questString().get().also { savingKeys.add(it) }
    }
}
