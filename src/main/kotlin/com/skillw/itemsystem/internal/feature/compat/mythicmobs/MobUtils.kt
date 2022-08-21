package com.skillw.itemsystem.internal.feature.compat.mythicmobs

import com.skillw.itemsystem.ItemSystem.itemBuilderManager
import com.skillw.itemsystem.internal.feature.ItemDrop.drop
import com.skillw.itemsystem.internal.feature.ItemDrop.effectDrop
import com.skillw.itemsystem.internal.feature.ItemUpdater.updateItem
import com.skillw.itemsystem.internal.feature.effect.RandomItemEffect
import com.skillw.itemsystem.internal.feature.product.DemandData
import com.skillw.itemsystem.internal.feature.product.ProductData
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import taboolib.platform.util.setEquipment
import taboolib.platform.util.toProxyLocation
import taboolib.type.BukkitEquipment

internal object MobUtils {
    private fun DropData.warning(obj: String) {
        taboolib.common.platform.function.warning("Wrong $obj in MythicMobs Mob $mobKey\'s Item System Config!")
    }

    internal fun DropData.equip(equipments: List<String>) {
        equipments.forEach { line ->
            if (!line.contains(" ")) warning("Equipment Line")
            val args = line.split(" ")
            val slotKey = args[0]
            val slot = BukkitEquipment.fromString(slotKey)
                ?: kotlin.run { warning("EquipmentSlot $slotKey"); return@forEach }

            val itemKey = args[1]
            val itemBuilder = itemBuilderManager[itemKey]
                ?: kotlin.run { warning("Item $itemKey"); return@forEach }

            val demand = line.substringAfter(' ').substringAfter(' ')
            val data = ProductData.product(itemBuilder, demand, this)
            val equipment = data.items[0]
            setEquipment(slot, equipment)
        }
    }

    internal fun DropData.drop(
        drops: List<String>,
        effect: Boolean,
        vector: Vector,
    ) {
        drops.flatMap { line ->
            current = if (line.startsWith("mob::")) this else killer
            val slotId = if (line.contains(" ")) line.substringBefore(' ') else line
            val slot = BukkitEquipment.fromString(slotId)
            if (slot == null) normalDrop(line) else equipmentDrop(slot, line.substringAfter(' '))
        }.run {
            if (effect) {
                effectDrop(RandomItemEffect(location.toProxyLocation()), vector, this@drop)
            } else {
                forEach {
                    it.drop(location, this@drop)
                }
            }
        }

    }

    private fun DropData.normalDrop(line: String): List<ItemStack> {
        val itemKey = line.substringBefore(' ')
        val itemBuilder = itemBuilderManager[itemKey]
            ?: kotlin.run { warning("Item $itemKey"); return emptyList() }
        val demand = line.substringAfter(' ').substringAfter(' ')
        return ProductData.product(itemBuilder, demand, this).items
    }

    private fun DropData.equipmentDrop(equipment: BukkitEquipment, line: String): List<ItemStack> {
        val (product, amount, isSame, data) = DemandData.demand(line.substringAfter(' '), current)
        if (!product) return emptyList()
        val item = equipment.getItem(current) ?: return emptyList()
        val build = fun(): ItemStack {
            return item.updateItem(current, productData = data)
        }
        return if (isSame) listOf(build().apply { this.amount = amount }) else Array(amount) { build() }.toList()
    }

}