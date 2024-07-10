package com.skillw.itemsystem.internal.feature.compat.mythicmobs

import com.skillw.itemsystem.internal.feature.ItemDrop
import com.skillw.itemsystem.internal.feature.compat.mythicmobs.MobUtils.equip
import ink.ptms.um.event.MobSpawnEvent
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.event.SubscribeEvent

object MobListener {
    @SubscribeEvent
    fun spawn(event: MobSpawnEvent) {
        val mob = event.mob
        val equipments = mob.config.getConfigurationSection("ItemSystem")?.getStringList("Equipment") ?: return
        (mob.entity as? LivingEntity)?.let {
            ItemDrop.DropData(it, mob.id).equip(equipments)
        }
    }

}
