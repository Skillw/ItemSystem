package com.skillw.itemsystem.internal.core.meta.bukkit.egg

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.entity.EntityType
import taboolib.common5.Coerce

@AutoRegister
object MetaSpawnType : BaseMeta("spawn-type") {

    override val priority = 3

    override fun invoke(memory: Memory) {
        with(memory) {
            val type = getString("spawn-type")
            builder.spawnType = Coerce.toEnum(type, EntityType::class.java, EntityType.UNKNOWN)
        }
    }

    override fun loadData(data: ItemData): Any? {
        return data.builder.spawnType?.name
    }

}
