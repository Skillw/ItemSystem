package com.skillw.itemsystem.internal.meta.eval

import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.annotation.AutoRegister

@AutoRegister
object MetaScript : BaseMeta("script") {
    override fun invoke(memory: Memory) {
        with(memory) {
            scriptManager.evalJs<Any?>(getString("script"), mapOf("data" to memory.processData))
        }
    }
}