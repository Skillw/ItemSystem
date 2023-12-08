package com.skillw.itemsystem.internal.feature.compat.attsystem

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.meta.data.Memory.Companion.get
import com.skillw.itemsystem.util.NBTUtils.toMutableMap
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData

@AutoRegister(test = "com.skillw.attsystem.AttributeSystem")
object ConditionsMeta : BaseMeta("conditions") {
    override fun invoke(memory: Memory) {
        with(memory) {
            val map = memory.get<Map<String, Any>>("conditions").analysis()
            nbt.getOrPut("CONDITION_DATA") { ItemTag() }.asCompound().putAll(ItemTagData.toNBT(map).asCompound())
        }
    }


    override fun loadData(data: ItemData): Any? {
        val conditions = data.itemTag.remove("CONDITION_DATA")?.asCompound()?.toMutableMap() ?: return null
        return mapOf("conditions" to conditions)
    }

}
