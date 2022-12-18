package com.skillw.itemsystem.internal.manager

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.manager.VarTypeManager
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.vartype.VariableType
import com.skillw.pouvoir.api.plugin.SubPouvoir
import java.util.function.Supplier

object VarTypeManagerImpl : VarTypeManager() {
    override val key: String = "VarTypeManager"
    override val priority: Int = 0
    override val subPouvoir: SubPouvoir = ItemSystem


    override fun register(key: String, value: VariableType) {
        value.alias.forEach {
            super.register(it, value)
        }
        super.register(key, value)
    }

    override fun createVar(memory: Memory): Supplier<Any>? {
        with(memory) {
            val key = memory.metaData["key"]?.toString() ?: return null
            val type = memory.metaData["type"]?.toString() ?: return null
            val cache = getBoolean("cache", true)
            val varType = ItemSystem.varTypeManager[type] ?: error("Unknown var type: $type")
            return Supplier<Any> {
                return@Supplier varType.createVar(memory).also { if (cache) processData[key] = it }
            }
        }
    }
}