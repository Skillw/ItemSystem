package com.skillw.itemsystem.internal.core.meta.eval

import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister

@AutoRegister
object MetaRun : BaseMeta("run") {
    override fun invoke(memory: Memory) {
        with(memory) {
            metaData["run"].toString().eval()
        }
    }
}
