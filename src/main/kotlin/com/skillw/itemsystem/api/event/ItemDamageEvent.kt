package com.skillw.itemsystem.api.event

import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * Item damage event 物品耐久损失事件
 *
 * @constructor Create empty Item damage event
 * @property entity 实体
 * @property itemStack 物品
 * @property slot 槽位
 * @property maxDurability 最大耐久
 * @property damage 损失耐久
 */
class ItemDamageEvent(
    val entity: LivingEntity,
    val itemStack: ItemStack,
    val slot: String,
    val maxDurability: Short,
    var damage: Int,
) :
    BukkitProxyEvent()