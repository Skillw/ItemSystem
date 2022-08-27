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
            val skill = Mythic.API.getSkillMechanic(skillKey)
            var trigger = "API"
            if (except("with")) {
                trigger = parseString()
            }
            val entity = (context["entity"] ?: context["player"]) as? LivingEntity? ?: return false
            return skill?.execute(Mythic.API.getSkillTrigger(trigger), entity, entity)
                ?: error("No such mm skill called $skillKey")
        }
    }
}