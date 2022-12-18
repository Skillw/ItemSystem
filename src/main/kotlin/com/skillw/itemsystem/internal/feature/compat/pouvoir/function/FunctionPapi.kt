package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.pouvoir.api.PouvoirAPI.placeholder
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.entity.LivingEntity

@AutoRegister
object FunctionPapi : PouFunction<String>("papi", namespace = "item_system") {

    override fun execute(parser: Parser): String {
        with(parser) {
            val str = parseString()
            val entity = context["entity"] as? LivingEntity? ?: return str
            return str.placeholder(entity)
        }
    }
}