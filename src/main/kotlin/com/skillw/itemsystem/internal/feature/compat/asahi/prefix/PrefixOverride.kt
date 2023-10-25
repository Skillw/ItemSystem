package com.skillw.itemsystem.internal.feature.compat.asahi.prefix

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.itemsystem.internal.core.builder.ProcessData

@AsahiPrefix(["override"], "item_system")
private fun override() = prefixParser {
    val key = questString()
    expect("=", "to")
    val value = questAny()
    result {
        if (this !is ProcessData) return@result "Error Context"
        this[key.get()] = value.get()
        savingKeys.add(key.get())
        value.get()
    }
}
