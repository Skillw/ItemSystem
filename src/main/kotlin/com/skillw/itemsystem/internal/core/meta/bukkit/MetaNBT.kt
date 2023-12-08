package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.meta.data.Memory.Companion.get
import com.skillw.itemsystem.util.NBTUtils.toMutableMap
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.util.valuesToTypeString
import taboolib.module.nms.ItemTagData

@AutoRegister
object MetaNBT : BaseMeta("nbt") {

    override val priority = 9999
    override val default = emptyMap<String, Any>()
    override fun invoke(memory: Memory) {
        with(memory) {
            val map = memory.get<Map<String, Any>>("nbt").analysis()
            nbt.putAll(ItemTagData.toNBT(map).asCompound())
        }
    }


    override fun loadData(data: ItemData): Map<String, Map<String, Any>>? {
        val nbt = data.itemTag.apply {
            remove("ITEM_SYSTEM")
        }.run {
            if (isNotEmpty()) {
                toMutableMap()
                    .valuesToTypeString()
            } else return null
        }
        return mapOf("nbt" to nbt)
    }


}
