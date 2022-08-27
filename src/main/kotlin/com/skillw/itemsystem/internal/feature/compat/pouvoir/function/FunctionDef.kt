package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.itemsystem.internal.core.builder.ProcessData
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser

@AutoRegister
object FunctionDef : PouFunction<Any>("def", "var", "let", "const", namespace = "item_system") {
    override fun execute(parser: Parser): Any? {
        with(parser) {
            if (context !is ProcessData) return "Error Context"
            val key = parseString()
            if (context.containsKey(key)) return context[key]
            except("=", "to")
            val value = parseAny()
            context[key] = value
            (context as ProcessData).savingKeys.add(key)
            return value
        }
    }
}