package com.skillw.itemsystem.internal.meta.bukkit.leather

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister
import org.bukkit.Color
import taboolib.common5.Coerce

@AutoRegister
object MetaColor : BaseMeta("color") {

    override val priority = 5

    override fun invoke(memory: Memory) {
        with(memory) {
            val color = getString("color")
            val splits = color.split(",")
            val red = Coerce.toInteger(splits[0])
            val green = Coerce.toInteger(splits[1])
            val blue = Coerce.toInteger(splits[2])
            builder.color = Color.fromRGB(red, green, blue)
        }
    }

    override fun loadData(data: ItemData): Any? {
        return data.builder.color?.run { "$red,$green,$blue" }
    }

}