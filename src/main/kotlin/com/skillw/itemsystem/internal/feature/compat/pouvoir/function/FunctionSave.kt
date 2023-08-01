package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.itemsystem.internal.core.builder.ProcessData
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister

@AutoRegister
object FunctionSave : PouFunction<Any>("save", namespace = "item_system") {
    override fun execute(parser: Parser): Any {
        with(parser) {
            if (context !is ProcessData) return "Error Context"
            val processData = context as ProcessData
            if (peek() == "[") {
                return parseList().onEach { processData.savingKeys.add(it.toString()) }
            }
            return parseString().also { processData.savingKeys.add(it) }
        }
    }
}
