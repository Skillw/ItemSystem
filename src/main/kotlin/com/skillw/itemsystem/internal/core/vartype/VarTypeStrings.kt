package com.skillw.itemsystem.internal.core.vartype

import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.vartype.VariableType
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.util.toList
import taboolib.common5.Coerce
import java.util.*


@AutoRegister
object VarTypeStrings : VariableType("strings", "string", "str") {

    private fun Any?.toStrList(): List<String> {
        return if (this is String)
            this.toList()
        else
            Coerce.toListOf(this, String::class.java)
    }

    override fun createVar(memory: Memory): Any {
        with(memory) {
            val content =
                when {
                    metaData.containsKey("values") -> {
                        LinkedList(metaData["values"].toStrList())
                    }

                    metaData.containsKey("strings") -> {
                        LinkedList(metaData["strings"].toStrList())
                    }

                    metaData.containsKey("value") -> {
                        metaData["value"].toString()
                    }

                    metaData.containsKey("string") -> {
                        metaData["string"].toString()
                    }

                    else -> {
                        return "Empty"
                    }
                }.analysis()
            return when (content) {
                is LinkedList<*> -> {
                    content as LinkedList<String>
                    object : MutableList<String> by content {
                        override fun get(index: Int): String {
                            return content[index]
                        }
                    }
                }

                else -> content
            }
        }
    }
}
