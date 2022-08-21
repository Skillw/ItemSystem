package com.skillw.itemsystem.internal.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.meta.data.Memory.Companion.get
import com.skillw.itemsystem.util.NBTUtils.toMutableMap
import com.skillw.itemsystem.util.TypeUtils.valuesToTypeString
import com.skillw.pouvoir.api.annotation.AutoRegister
import taboolib.module.nms.ItemTag

@AutoRegister
object MetaNBT : BaseMeta("nbt") {

    override val priority = 999
    override val default = emptyMap<String, Any>()
    override fun invoke(memory: Memory) {
        with(memory) {
            val map = memory.get<Map<String, Any>>("nbt").analysis()
            nbt.putAll(ItemTag.toNBT(map).asCompound())
        }
    }


    override fun loadData(data: ItemData): Any {
        return data.itemTag.apply {
            remove("ITEM_SYSTEM")
        }
            .toMutableMap()
            .valuesToTypeString()
    }


}