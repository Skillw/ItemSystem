package com.skillw.itemsystem.internal.core.meta.bukkit.skull

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.meta.data.Memory.Companion.get
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import taboolib.library.xseries.XSkull.SkullTexture
import taboolib.platform.util.ItemBuilder
import java.util.*

@AutoRegister
object MetaSkullTexture : BaseMeta("skull-texture") {

    override val priority = 3

    override fun invoke(memory: Memory) {
        with(memory) {
            val skull = get<Map<String, Any>>("skull-texture")
            val texture = skull["texture"] as? String? ?: return
            val uuid = UUID.fromString(skull["uuid"] as? String? ?: return)
            builder.skullTexture = SkullTexture(texture,uuid)
        }
    }

    override fun loadData(data: ItemData): Any? {
        val skull = data.builder.skullTexture ?: return null
        return mapOf("skull-texture" to linkedMapOf("texture" to skull.texture, "uuid" to skull.uuid.toString()))
    }

}
