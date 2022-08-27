package com.skillw.itemsystem.internal.feature

import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir

object ItemCache {
    private val loreCache = HashMap<Int, MutableList<String>>()

    private val tagCache = HashMap<Int, ItemTag>()

    @ScriptTopLevel
    @JvmStatic
    fun ItemStack.cacheLore(): MutableList<String> {
        return ArrayList(loreCache.getOrPut(hashCode()) { lore ?: mutableListOf() })
    }

    @ScriptTopLevel
    @JvmStatic
    fun ItemStack.cacheTag(): ItemTag {
        if (isAir()) return ItemTag()
        return tagCache.getOrPut(hashCode()) { getItemTag() }
    }
}