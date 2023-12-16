package com.skillw.itemsystem.internal.feature.compat.asahi.prefix

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.itemsystem.internal.feature.ItemDynamic.addDynamic

@AsahiPrefix(["dynamic"], "item_system")
private fun dynamic() = prefixParser<String> {
    val script = splitTill("{", "}")
    result {
        addDynamic(script[0])
    }
}
