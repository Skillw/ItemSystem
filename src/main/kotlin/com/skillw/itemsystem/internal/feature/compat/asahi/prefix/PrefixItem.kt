package com.skillw.itemsystem.internal.feature.compat.asahi.prefix

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@AsahiPrefix(["item"], "common")
private fun item() = prefixParser {
    val material = quest<Material>()
    result {
        ItemStack(material.get())
    }
}
