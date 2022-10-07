package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.meta.data.Memory.Companion.get
import com.skillw.pouvoir.api.annotation.AutoRegister
import java.util.*

@AutoRegister
object MetaUnique : BaseMeta("unique") {
    override fun invoke(memory: Memory) {
        with(memory) {
            if (get("unique", false)) {
                processData["unique"] = UUID.randomUUID().toString()
                savingKeys.add("unique")
            }
        }
    }
}