package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quester
import com.skillw.itemsystem.internal.core.builder.ProcessData

@AsahiPrefix(["def"], "item_system")
private fun def() = prefixParser<Any> {
    quester {
        if (context() !is ProcessData) {
            error("Error Context")
        }
        val key = next()
        if (containsKey(key)) result { get(key) }
        expect("=", "to")
        val value = questAny()
        put(key, value)
        (context() as ProcessData).savingKeys.add(key)
        result { value }
    }
}
