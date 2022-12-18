package com.skillw.itemsystem.api.builder

import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.platform.util.ItemBuilder

/**
 * 序列化物品时用到的数据类
 *
 * @constructor 创建一个新的物品数据类
 * @property itemStack 物品
 * @property itemTag 物品的数据
 * @property builder 物品构造器
 */
data class ItemData constructor(val itemStack: ItemStack, val itemTag: ItemTag, val builder: ItemBuilder) {
    companion object {
        fun create(itemStack: ItemStack): ItemData {
            return ItemData(itemStack, itemStack.getItemTag(), ItemBuilder(itemStack))
        }
    }
}
