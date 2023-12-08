package com.skillw.itemsystem.internal.feature.compat.asahi.prefix

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.itemsystem.internal.core.builder.ProcessData

@AsahiPrefix(["var", "let", "const"], "item_system")
private fun let() = prefixParser {
    val key = next()
    expect("=", "to")
    val value = questAny()
    result {
        val data = get("data") as? ProcessData ?: return@result "Error Context"
        if (containsKey(key)) return@result this[key]
        this[key] = value.get()
        data.savingKeys.add(key)
        value.get()
    }
}
