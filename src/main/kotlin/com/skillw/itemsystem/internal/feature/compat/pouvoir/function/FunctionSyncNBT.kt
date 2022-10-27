package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser

@AutoRegister
object FunctionSyncNBT : PouFunction<String>("syncNBT", namespace = "item_system") {

    override fun execute(parser: Parser): String {
        with(parser) {
            val nbt = parseString()
            val content = "&item nbt get '${nbt}'"
            return "{{_dynamic::${content}_}}"
        }
    }
}