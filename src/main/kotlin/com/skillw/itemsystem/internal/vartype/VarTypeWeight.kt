package com.skillw.itemsystem.internal.vartype

import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.vartype.VariableType
import com.skillw.pouvoir.api.annotation.AutoRegister
import taboolib.common5.Coerce
import taboolib.common5.RandomList


@AutoRegister
object VarTypeWeight : VariableType("weight") {

    override fun createVar(memory: Memory): Any {
        with(memory) {
            val weights = RandomList<Any>()
            (metaData["values"] as List<Any>).forEach {
                it as? Map<Any, Any>? ?: return@forEach
                val weight = Coerce.toInteger(it.keys.first().toString().analysis())
                val value = it.values.first()
                weights.add(value, weight)
            }
            return weights.random() ?: "Empty Weight"
        }
    }
}