package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@AutoRegister
object FunctionItem : PouFunction<ItemStack>("item", namespace = "common") {
    override fun execute(parser: Parser): ItemStack {
        with(parser) {
            return ItemStack(parse<Material>())
        }
    }
}
