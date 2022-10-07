package com.skillw.itemsystem.internal.feature

import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir

object ItemCache {
    private val loreCache = HashMap<Int, MutableList<String>>()

    @ScriptTopLevel
    @JvmStatic
    fun ItemStack.cacheLore(): MutableList<String> {
        return ArrayList(loreCache.getOrPut(hashCode()) {
            if (hasItemMeta()) itemMeta.lore ?: mutableListOf() else mutableListOf()
        })
    }

    @ScriptTopLevel
    @JvmStatic
    @Deprecated("Use getTag")
    fun ItemStack.cacheTag(): ItemTag {
        return getTag()
    }

    @ScriptTopLevel
    @JvmStatic
    fun ItemStack.getTag(): ItemTag {
        if (isAir()) return ItemTag()
        return getItemTag()
    }


}