package com.skillw.itemsystem.internal.core.meta.action

import com.skillw.itemsystem.api.action.ActionType
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.meta.data.Memory.Companion.get
import com.skillw.pouvoir.api.annotation.AutoRegister
import taboolib.common.platform.function.warning
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagList
import java.util.regex.Pattern

@AutoRegister
object MetaAction : BaseMeta("action") {
    override val priority = 12
    private val varPattern = Pattern.compile("\\$(?<var>[a-zA-Z1-9]+)")
    override fun invoke(memory: Memory) {
        with(memory) {
            val context = get<Map<String, Any>>("action")
            val typeStr = context["type"].toString().analysis()
            val type = ActionType.actionTypes[typeStr]
            if (type == null) {
                warning("Unknown action type: $typeStr")
                return
            }
            val run = context["run"]?.toString() ?: return
            nbt.getOrPut("ITEM_SYSTEM") { ItemTag() }.asCompound()
                .getOrPut("actions") { ItemTag() }.asCompound()
                .getOrPut(type.key) { ItemTagList() }.asList()
                .add(ItemTagData.toNBT(run))
        }
    }


}