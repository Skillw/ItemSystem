package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.util.script.ColorUtil.decolored
import taboolib.module.chat.colored
import taboolib.module.nms.getName

@AutoRegister
object MetaDisplay : BaseMeta("display") {
    override val priority = 1

    override val default = ""

    override fun invoke(memory: Memory) {
        with(memory) {
            builder.name = getString("display").colored()
        }
    }

    override fun loadData(data: ItemData): Any {
        data.itemTag.remove("display")
        return (data.builder.name ?: data.itemStack.getName()).decolored()
    }

}
