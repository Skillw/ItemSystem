package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister

@AutoRegister
object MetaModelData : BaseMeta("data") {

    override val priority = 4
    override val default = -1

    override fun invoke(memory: Memory) {
        with(memory) {
            builder.customModelData = getInt("data")
        }
    }

    override fun loadData(data: ItemData): Any {
        data.itemTag.remove("CustomModelData")
        return data.builder.customModelData
    }

}