package com.skillw.itemsystem.internal.core.vartype

import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.vartype.VariableType
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.util.randomInt
import taboolib.common5.Coerce


@AutoRegister
object VarTypeRandom : VariableType("random") {

    override fun createVar(memory: Memory): Any {
        with(memory) {
            return Coerce.toListOf(metaData["values"], String::class.java).run {
                get(randomInt(0, size - 1).analysis())
            }
        }
    }
}
