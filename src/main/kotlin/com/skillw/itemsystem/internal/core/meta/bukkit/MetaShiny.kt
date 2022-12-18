package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister

@AutoRegister
object MetaShiny : BaseMeta("shiny") {

    override val priority = 7
    override val default = false

    override fun invoke(memory: Memory) {
        with(memory) {
            if (getBoolean("shiny"))
                builder.shiny()
        }
    }

}