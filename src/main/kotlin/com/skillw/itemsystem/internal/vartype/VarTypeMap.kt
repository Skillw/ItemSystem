package com.skillw.itemsystem.internal.vartype

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.vartype.VariableType
import com.skillw.itemsystem.internal.meta.data.MetaData
import com.skillw.itemsystem.internal.meta.define.MetaDefine
import com.skillw.pouvoir.api.annotation.AutoRegister
import java.util.function.Supplier


@AutoRegister
object VarTypeMap : VariableType("map") {
    private val ignoreKeys = arrayOf("var", "cache", "save", "key")

    @Suppress("UNCHECKED_CAST")
    override fun createVar(memory: Memory): Any {
        with(memory) {
            val map = HashMap<String, Any>().apply {
                putAll(memory.metaData.map
                    .filterKeys { it !in ignoreKeys }.mapValues {
                        val value = it.value
                        if (value !is Map<*, *>) return@mapValues value
                        value as Map<String, Any>
                        if (!value.containsKey("key")) return@mapValues value
                        val metaData = MetaData(MetaDefine).apply { putAll(value) }
                        ItemSystem.varTypeManager.createVar(Memory(metaData, processData)) ?: value
                    })
            }
            return object : MutableMap<String, Any> by map {
                override fun get(key: String): Any? {
                    return map[key]?.let {
                        when (it) {
                            is Supplier<*> -> it.get().analysis()
                            else -> it.analysis()
                        }
                    }
                }
            }
        }
    }
}