package com.skillw.itemsystem.api.event

import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * Item mend event 物品修复事件
 *
 * @constructor Create empty Item mend event
 * @property entity 实体
 * @property itemStack 物品
 * @property maxDurability 最大耐久
 * @property repairAmount 修复耐久
 */
class ItemMendEvent(
    val entity: LivingEntity,
    val itemStack: ItemStack,
    val maxDurability: Short,
    var repairAmount: Int,
) :
    BukkitProxyEvent()