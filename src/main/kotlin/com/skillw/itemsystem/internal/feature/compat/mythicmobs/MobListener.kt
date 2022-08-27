package com.skillw.itemsystem.internal.feature.compat.mythicmobs

import com.skillw.itemsystem.internal.feature.ItemDrop
import com.skillw.itemsystem.internal.feature.compat.mythicmobs.MobUtils.drop
import com.skillw.itemsystem.internal.feature.compat.mythicmobs.MobUtils.equip
import com.skillw.pouvoir.api.PouvoirAPI.analysis
import ink.ptms.um.event.MobDeathEvent
import ink.ptms.um.event.MobSpawnEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.Coerce

object MobListener {
    @SubscribeEvent
    fun spawn(event: MobSpawnEvent) {
        val mob = event.mob
        val equipments = mob.config.getConfigurationSection("ItemSystem")?.getStringList("Equipment") ?: return
        (mob.entity as? LivingEntity)?.let { ItemDrop.DropData(it, mob.id).equip(equipments) }
    }

    @SubscribeEvent
    fun drop(event: MobDeathEvent) {
        val mob = event.mob
        val killer = event.killer ?: return
        val options = mob.config.getConfigurationSection("ItemSystem") ?: return
        val drops = options.getStringList("Drops")
        val effect = options.getBoolean("Effect")
        val x = Coerce.toDouble(options.getString("offsetX", "0.0")!!.analysis())
        val y = Coerce.toDouble(options.getString("offsetY", "0.0")!!.analysis())
        val z = Coerce.toDouble(options.getString("offsetZ", "0.0")!!.analysis())
        (mob.entity as? LivingEntity)?.let {
            ItemDrop.DropData(it, mob.id, killer).drop(
                drops,
                effect,
                Vector(x, y, z)
            )
        }
    }
}