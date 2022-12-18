package com.skillw.itemsystem.internal.feature.compat.attsystem

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.meta.data.Memory.Companion.get
import com.skillw.itemsystem.util.NBTUtils.toMutableMap
import com.skillw.pouvoir.api.annotation.AutoRegister
import taboolib.module.nms.ItemTag

@AutoRegister(test = "com.skillw.attsystem.AttributeSystem")
object AttributesMeta : BaseMeta("attributes") {
    override fun invoke(memory: Memory) {
        with(memory) {
            val map = memory.get<Map<String, Any>>("attributes").analysis()
            nbt.getOrPut("ATTRIBUTE_DATA") { ItemTag() }.asCompound().putAll(ItemTag.toNBT(map).asCompound())
        }
    }


    override fun loadData(data: ItemData): Any? {
        val attributes = (data.itemTag.remove("ATTRIBUTE_DATA")?.asCompound()?.toMutableMap()) ?: return null
        return mapOf(
            "attributes" to attributes
        )
    }

}