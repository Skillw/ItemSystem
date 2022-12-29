package com.skillw.itemsystem.internal.feature


import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir

object ItemCache {
    private val loreCache = HashMap<Int, MutableList<String>>()

    
    @JvmStatic
    fun ItemStack.cacheLore(): MutableList<String> {
        return ArrayList(loreCache.getOrPut(hashCode()) {
            if (hasItemMeta()) itemMeta.lore ?: mutableListOf() else mutableListOf()
        })
    }

    
    @JvmStatic
    fun ItemStack.getTag(): ItemTag {
        if (isAir()) return ItemTag()
        return getItemTag()
    }


}