package com.skillw.itemsystem.internal.feature.compat.asahi.prefix

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.pouvoir.api.PouvoirAPI.placeholder
import org.bukkit.entity.LivingEntity

@AsahiPrefix(["papi"], "item_system")
private fun papi() = prefixParser<String> {
    val str = questString()
    result {
        val entity = this["entity"] as? LivingEntity? ?: return@result str.get()
        str.get().placeholder(entity)
    }
}
