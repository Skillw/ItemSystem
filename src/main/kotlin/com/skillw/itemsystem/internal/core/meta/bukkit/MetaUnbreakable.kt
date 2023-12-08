package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister

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
        data.itemTag.remove("Unbreakable")
        return data.builder.isUnbreakable
    }

}
