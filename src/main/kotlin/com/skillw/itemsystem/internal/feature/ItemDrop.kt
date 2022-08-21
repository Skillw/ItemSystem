package com.skillw.itemsystem.internal.feature

import com.skillw.itemsystem.internal.feature.ItemCache.cacheTag
import com.skillw.itemsystem.internal.feature.compat.mythicmobs.DropData
import com.skillw.itemsystem.internal.feature.effect.RandomItemEffect
import ink.ptms.um.Mythic
import org.bukkit.Location
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.toBukkitLocation

object ItemDrop {
    @JvmStatic
    internal fun ItemStack.drop(location: Location, data: DropData): Item {
        val skills = cacheTag().getDeep("ITEM_SYSTEM.drop-skills")?.asList()
        skills?.map { it.asString() }?.forEach { line ->
            Mythic.API.castSkill(data.entity, line, data.killer, location)
        }
        return location.world.dropItem(location, this)
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