package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser

@AutoRegister
object FunctionDynamic : PouFunction<String>("dynamic", namespace = "item_system") {

    override fun execute(parser: Parser): String {
        with(parser) {
            val content = splitTill("{", "}")
            return "{{_dynamic::${content}_}}"
        }
    }
}