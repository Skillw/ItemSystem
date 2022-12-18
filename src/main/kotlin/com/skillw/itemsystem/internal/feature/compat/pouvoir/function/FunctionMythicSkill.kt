package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import ink.ptms.um.Mythic
import org.bukkit.entity.LivingEntity

@AutoRegister
object FunctionMythicSkill : PouFunction<Boolean>("mmskill", namespace = "common") {
    override fun execute(parser: Parser): Boolean {
        with(parser) {
            val skillKey = parseString()
            var power = 1.0f
            if (except("with")) {
                power = parseFloat()
            }
            val caster = (context["entity"] ?: context["player"]) as? LivingEntity? ?: return false
            return Mythic.API.castSkill(caster, skillKey, null, power = power)
        }
    }
}