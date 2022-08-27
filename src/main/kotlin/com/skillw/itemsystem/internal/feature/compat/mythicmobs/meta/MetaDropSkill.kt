package com.skillw.itemsystem.internal.feature.compat.mythicmobs.meta

import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister
import taboolib.module.nms.ItemTagData

@AutoRegister
object MetaDropSkill : BaseMeta("drop-skill") {
    override fun invoke(memory: Memory) {
        with(memory) {
            val skill = getList("drop-skills").map { it.toString() }
            nbt.putDeep("ITEM_SYSTEM.drop-skills", ItemTagData.toNBT(skill))
        }
    }

}