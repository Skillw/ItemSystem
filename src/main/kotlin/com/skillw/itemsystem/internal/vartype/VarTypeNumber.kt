package com.skillw.itemsystem.internal.vartype

import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.vartype.VariableType
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.util.NumberUtils.format
import java.lang.Double.max
import java.lang.Double.min


@AutoRegister
object VarTypeNumber : VariableType("number", "num") {

    override fun createVar(memory: Memory): Any {
        with(memory) {
            val number = getDouble("value", 0.0)
            val format = getString("format", "#.##")
            val max = getDouble("max", number)
            val min = getDouble("min", number)
            return max(min(number, max), min).format(format)
        }
    }
}