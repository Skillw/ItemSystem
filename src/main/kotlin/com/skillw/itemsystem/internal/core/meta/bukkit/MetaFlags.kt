package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.inventory.ItemFlag
import taboolib.common5.Coerce

@AutoRegister
object MetaFlags : BaseMeta("flags") {

    override val priority = 8

    override val default = emptyList<String>()

    override fun invoke(memory: Memory) {
        with(memory) {
            builder.flags.addAll(getList("flags").map {
                Coerce.toEnum(it, ItemFlag::class.java)
            })
        }
    }

    override fun loadData(data: ItemData): Any {
        data.itemTag.remove("HideFlags")
        return data.builder.flags.map { it.name }
    }

}
