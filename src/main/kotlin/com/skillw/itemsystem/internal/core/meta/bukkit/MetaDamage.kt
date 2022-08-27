package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister

@AutoRegister
object MetaDamage : BaseMeta("damage") {
    override val priority = 3
    override val default = 0
    override fun invoke(memory: Memory) {
        with(memory) {
            builder.damage = getInt("damage")
        }
    }

    override fun loadData(data: ItemData): Any {
        data.itemTag.remove("Damage")
        return data.builder.damage
    }

}