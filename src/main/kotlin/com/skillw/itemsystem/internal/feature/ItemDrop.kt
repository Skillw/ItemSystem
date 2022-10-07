package com.skillw.itemsystem.internal.feature

import com.skillw.itemsystem.api.event.ItemDropEvent
import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.itemsystem.internal.feature.effect.RandomItemEffect
import ink.ptms.um.Mythic
import org.bukkit.Location
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.toBukkitLocation

object ItemDrop {

    internal data class DropData(
        val entity: LivingEntity,
        val mobKey: String = "",
        val killer: LivingEntity = entity,
        var current: LivingEntity = entity,
    ) :
        LivingEntity by entity

    @JvmStatic
    internal fun ItemStack.drop(location: Location, data: DropData): Item {
        val skills = getTag().getDeep("ITEM_SYSTEM.drop-skills")?.asList()
        skills?.map { it.asString() }?.forEach { line ->
            Mythic.API.castSkill(data.entity, line, data.killer, location)
        }
        val event = ItemDropEvent(data.entity, location.world.dropItem(location, this))
        event.call()
        return event.item
    }


    @JvmStatic
    internal fun List<ItemStack>.effectDrop(
        randomItemEffect: RandomItemEffect,
        itemVector: Vector = Vector(0.0, 0.0, 0.0),
        data: DropData,
    ) {
        val location = randomItemEffect.origin.toBukkitLocation()
        randomItemEffect.items.addAll(
            map { itemStack ->
                itemStack.drop(location, data)
                    .apply { velocity = itemVector.add(Vector.getRandom()).add(Vector(0, 2, 0)) }
            }
        )
        submitAsync {
            randomItemEffect.play()
        }
    }

}