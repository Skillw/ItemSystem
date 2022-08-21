package com.skillw.itemsystem.internal.feature.compat.mythicmobs

import org.bukkit.entity.LivingEntity

internal data class DropData(
    val entity: LivingEntity,
    val mobKey: String = "",
    val killer: LivingEntity = entity,
    var current: LivingEntity = entity,
) :
    LivingEntity by entity
