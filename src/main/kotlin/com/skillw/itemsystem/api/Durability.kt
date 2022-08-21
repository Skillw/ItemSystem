package com.skillw.itemsystem.api

import com.skillw.itemsystem.api.event.ItemDamageEvent
import com.skillw.itemsystem.api.event.ItemDurabilityEvent
import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

@ScriptTopLevel
object Durability {
    /**
     * 获取物品耐久度
     *
     * @return Int 耐久度
     * @receiver ItemStack 物品
     */
    @JvmStatic
    fun ItemStack.durability(): Int {
        val max = type.maxDurability.toInt()
        return ItemDurabilityEvent.Get(
            this,
            max,
            max - durability.toInt()
        ).run { call(); durability }
    }

    /**
     * 获取物品最大耐久度
     *
     * @return Int 最大耐久度
     * @receiver ItemStack 物品
     */

    @JvmStatic
    fun ItemStack.maxDurability(): Int {
        val max = type.maxDurability.toInt()
        return ItemDurabilityEvent.Get(
            this,
            max,
            max - durability.toInt()
        ).run { call(); maxDurability }
    }

    /**
     * 设置物品耐久度
     *
     * 注意是耐久度而不是损坏值
     *
     * @param durability Int 耐久度
     * @return ItemStack 物品
     * @receiver ItemStack 物品
     */
    @JvmStatic
    fun ItemStack.durability(durability: Int): ItemStack {
        ItemDurabilityEvent.Set(
            this,
            durability
        ).run {
            call();
            this.durability = maxDurability() - durability
        }
        return this
    }

    /**
     * 减少物品耐久度
     *
     * @param damage Int 损坏值
     * @param entity LivingEntity 实体
     * @param slot String? 槽位(可能原版槽位，也可能是AS的某个装备槽)
     * @return ItemStack 物品
     * @receiver ItemStack 物品
     */
    @JvmStatic
    fun ItemStack.damage(damage: Int, entity: LivingEntity, slot: String? = null): ItemStack {
        val event = ItemDamageEvent(entity, this, slot.toString(), maxDurability().toShort(), damage)
        if (!event.call()) return this
        durability(durability() - damage)
        return this
    }
}