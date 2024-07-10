package com.skillw.itemsystem.internal.feature.compat.mythicmobs.legacy

import com.skillw.asahi.api.AsahiAPI.analysis
import com.skillw.itemsystem.internal.feature.ItemDrop
import com.skillw.itemsystem.internal.feature.compat.mythicmobs.MobUtils.drop
import com.skillw.itemsystem.internal.feature.compat.mythicmobs.MobUtils.equip
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.Coerce

object FourListener {

    @SubscribeEvent
    fun onMobSpawn(event: MythicMobSpawnEvent) {
        val mob = event.mob.type
        val equipments = mob.config.getNestedConfig("ItemSystem")?.getStringList("Equipment") ?: return
        (event.mob.entity.bukkitEntity as? LivingEntity)?.let {
            ItemDrop.DropData(it, event.mob.type.internalName).equip(equipments)
        }
    }

    @SubscribeEvent
    fun onMobDeath(event: MythicMobDeathEvent) {
        val mob = event.mob.type
        val killer = event.killer ?: return
        val options = mob.config.getNestedConfig("ItemSystem") ?: return
        val drops = options.getStringList("Drops")
        val effect = options.getBoolean("Effect")
        val x = Coerce.toDouble(options.getString("offsetX", "0.0")!!.analysis())
        val y = Coerce.toDouble(options.getString("offsetY", "0.0")!!.analysis())
        val z = Coerce.toDouble(options.getString("offsetZ", "0.0")!!.analysis())
        (event.mob.entity.bukkitEntity as? LivingEntity)?.let {
            ItemDrop.DropData(it, mob.internalName, killer).drop(
                drops,
                effect,
                Vector(x, y, z)
            )
        }
    }
}