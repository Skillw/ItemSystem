package com.skillw.itemsystem.internal.core.meta.define

import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.internal.manager.VarTypeManagerImpl.createVar
import com.skillw.pouvoir.api.annotation.AutoRegister

@AutoRegister
object MetaDefine : BaseMeta("define") {
    override fun invoke(memory: Memory) {
        with(memory) {
            val key = memory.metaData["key"]?.toString() ?: return
            val override = getBoolean("override", false)
            if (getBoolean("save", getBoolean("cache", false))) {
                savingKeys += key
                if (processData.containsKey(key) && !override) {
                    return
                }
            }
            createVar(memory)?.let { processData[key] = it }
        }
    }


}