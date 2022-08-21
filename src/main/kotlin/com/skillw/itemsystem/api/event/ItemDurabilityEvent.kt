package com.skillw.itemsystem.api.event

import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * @className ItemDamageEvent
 *
 * @author Glom
 * @date 2022/8/19 7:20 Copyright 2022 user. All rights reserved.
 */
class ItemDurabilityEvent() {

    /**
     * 耐久获取事件
     *
     * @property itemStack 被获取耐久的物品
     * @property maxDurability 最大耐久
     * @property durability 当前耐久
     */
    class Get(
        val itemStack: ItemStack,
        var maxDurability: Int,
        var durability: Int,
    ) : BukkitProxyEvent()

    /**
     * 设置耐久事件
     *
     * @property itemStack 被设置耐久的物品
     * @property durability 设置的耐久
     */
    class Set(
        val itemStack: ItemStack,
        var durability: Int,
    ) : BukkitProxyEvent()

}