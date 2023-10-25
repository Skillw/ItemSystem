package com.skillw.itemsystem.internal.feature.compat.asahi.prefix

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.itemsystem.internal.core.builder.ProcessData

@AsahiPrefix(["def", "var", "let", "const"], "item_system")
private fun def() = prefixParser {
    val key = next()
    expect("=", "to")
    val value = questAny()
    result {
        if (this !is ProcessData) return@result "Error Context"
        if (containsKey(key)) return@result this[key]
        this[key] = value.get()
        savingKeys.add(key)
        value.get()
    }
}
