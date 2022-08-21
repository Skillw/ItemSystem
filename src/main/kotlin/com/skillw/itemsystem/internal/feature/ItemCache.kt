package com.skillw.itemsystem.internal.feature

import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir

object ItemCache {
    private val loreCache = HashMap<Int, MutableList<String>>()

    private val tagCache = HashMap<Int, ItemTag>()

    fun ItemStack.cacheLore(): MutableList<String> {
        return ArrayList(loreCache.getOrPut(hashCode()) { lore ?: mutableListOf() })
    }

    fun ItemStack.cacheTag(): ItemTag {
        if (isAir()) return ItemTag()
        return tagCache.getOrPut(hashCode()) { getItemTag() }
    }
}