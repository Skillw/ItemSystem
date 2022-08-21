package com.skillw.itemsystem.internal.feature.listener

import com.skillw.itemsystem.api.Durability.damage
import com.skillw.itemsystem.api.event.ItemMendEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerItemMendEvent
import org.bukkit.inventory.meta.Damageable
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.attacker
import taboolib.platform.util.getEquipment
import taboolib.platform.util.setEquipment
import taboolib.type.BukkitEquipment
import kotlin.math.max

private object DurabilityListener {
    private val ignoredCauses = listOf(
        DamageCause.DROWNING,
        DamageCause.SUICIDE,
        DamageCause.FALL,
        DamageCause.VOID,
        DamageCause.FIRE_TICK,
        DamageCause.SUFFOCATION,
        DamageCause.POISON,
        DamageCause.WITHER,
        DamageCause.STARVATION,
        DamageCause.MAGIC
    )

    private val armorSlots =
        arrayOf(BukkitEquipment.HEAD, BukkitEquipment.CHEST, BukkitEquipment.LEGS, BukkitEquipment.FEET)

    @SubscribeEvent(ignoreCancelled = true)
    fun playerDamage(event: EntityDamageEvent) {
        if (ignoredCauses.contains(event.cause)) return
        val entity = event.entity as? LivingEntity? ?: return
        val damage = max(event.damage.toInt() / 4, 1)
        armorSlots.forEach { entity.durabilityLess(it, damage) }
    }

    @SubscribeEvent
    fun playerDig(event: BlockBreakEvent) {
        event.player.durabilityLess(BukkitEquipment.HAND, 1)
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun playerMeleeAttack(event: EntityDamageByEntityEvent) {
        event.attacker?.durabilityLess(BukkitEquipment.HAND, 1)
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun playerBowAttack(event: EntityShootBowEvent) {
        event.entity.durabilityLess(BukkitEquipment.HAND, 1)
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun itemDamage(event: PlayerItemDamageEvent) {
        event.isCancelled = true
        event.player.durabilityLess(BukkitEquipment.HAND, 1)
    }

    @SubscribeEvent(EventPriority.HIGH, ignoreCancelled = true)
    fun mendEvent(event: PlayerItemMendEvent) {
        event.isCancelled = true
        val item = event.item
        val mendEvent = ItemMendEvent(event.player, item, item.type.maxDurability, event.repairAmount)
        if (!mendEvent.call()) return
        event.item.apply {
            itemMeta = itemMeta.apply {
                (itemMeta as Damageable).apply {
                    damage = max(0, damage - event.repairAmount)
                }
            }
        }
    }

    private fun LivingEntity.durabilityLess(slot: BukkitEquipment, damage: Int) {
        val item = getEquipment(slot) ?: return
        if (!item.hasItemMeta()) return
        if (item.itemMeta !is Damageable) return
        setEquipment(slot, item.damage(damage, this, slot.name))
    }

}