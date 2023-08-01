package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.itemsystem.internal.feature.ItemDynamic.addDynamic
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister

@AutoRegister
object FunctionSyncNBT : PouFunction<String>("syncNBT", namespace = "item_system") {

    override fun execute(parser: Parser): String {
        with(parser) {
            val nbt = parseString()
            val content = "&item nbt get '${nbt}'"
            return addDynamic(content)
        }
    }
}
