package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.ChatColor
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce
import taboolib.module.chat.colored
import taboolib.module.nms.ItemTagData

@AutoRegister
object MetaGlowColor : BaseMeta("glow-color") {

    override val priority = 5

    override fun invoke(memory: Memory) {
        with(memory) {
            val colorStr = getString("glow-color")
            val color =
                colorStr.replace("&", "").let { ChatColor.getByChar(it) ?: Coerce.toEnum(it, ChatColor::class.java) }
            if (color == null) {
                warning("&c${colorStr} is a valid color".colored())
            }
            nbt.putDeep("ITEM_SYSTEM.glow-color", ItemTagData.toNBT(color.name))
        }
    }

    override fun loadData(data: ItemData): Any? {
        return data.itemTag.removeDeep("ITEM_SYSTEM.glow-color")?.unsafeData()
    }

}
