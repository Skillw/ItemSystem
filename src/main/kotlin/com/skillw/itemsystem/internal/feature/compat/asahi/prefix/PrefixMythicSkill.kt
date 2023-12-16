package com.skillw.itemsystem.internal.feature.compat.asahi.prefix

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quester
import ink.ptms.um.Mythic
import org.bukkit.entity.LivingEntity

@AsahiPrefix(["mmskill"], "common")
private fun mmskill() = prefixParser<Boolean> {
    val skillKey = questString()
    val power = if (expect("with")) questFloat() else quester { 1.0f }
    result {
        val caster = (this["entity"] ?: this["player"]) as? LivingEntity? ?: return@result false
        Mythic.API.castSkill(caster, skillKey.get(), null, power = power.get())
    }
}
