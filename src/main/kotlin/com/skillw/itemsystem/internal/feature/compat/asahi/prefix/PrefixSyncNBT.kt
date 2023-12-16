package com.skillw.itemsystem.internal.feature.compat.asahi.prefix

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.itemsystem.internal.feature.ItemDynamic.addDynamic

@AsahiPrefix(["syncNBT"], "common")
private fun syncNBT() = prefixParser<String> {
    val nbt = questString()
    result {
        val content = "&item nbt get '${nbt.get()}'"
        return@result addDynamic(content)
    }
}
