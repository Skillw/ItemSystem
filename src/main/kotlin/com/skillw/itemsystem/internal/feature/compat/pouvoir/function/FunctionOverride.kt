package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quester
import com.skillw.itemsystem.internal.core.builder.ProcessData

@AsahiPrefix(["override"], "item_system")
private fun override() = prefixParser<Any> {
    quester {
        if (context() !is ProcessData) {
            error("Error Context")
        }
        val key = questString()
        expect("=", "to")
        val value = questAny()
        put(key.get(), value)
        (context() as ProcessData).savingKeys.add(key.get())
        result { value }
    }
}
