package com.skillw.itemsystem.internal.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister

@AutoRegister
object MetaUnbreakable : BaseMeta("unbreakable") {

    override val priority = 7
    override val default = false

    override fun invoke(memory: Memory) {
        with(memory) {
            builder.isUnbreakable = getBoolean("unbreakable")
        }
    }

    override fun loadData(data: ItemData): Any {
        return data.builder.isUnbreakable
    }

}