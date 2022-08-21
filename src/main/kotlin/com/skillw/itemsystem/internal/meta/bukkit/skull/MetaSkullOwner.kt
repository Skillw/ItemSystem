package com.skillw.itemsystem.internal.meta.bukkit.skull

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister

@AutoRegister
object MetaSkullOwner : BaseMeta("skull-owner") {

    override val priority = 3

    override fun invoke(memory: Memory) {
        with(memory) {
            val skullOwner = getString("skull-owner")
            builder.skullOwner = skullOwner
        }
    }

    override fun loadData(data: ItemData): Any? {
        return data.builder.skullOwner
    }

}