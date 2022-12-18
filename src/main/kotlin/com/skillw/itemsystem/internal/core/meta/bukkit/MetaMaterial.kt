package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.taboolib.library.xseries.XMaterial
import org.bukkit.Material

@AutoRegister
object MetaMaterial : BaseMeta("material") {

    override val priority = 2

    override fun invoke(memory: Memory) {
        with(memory) {
            val material = getString("material")
            builder.material = Material.matchMaterial(material.uppercase()) ?: XMaterial.matchXMaterial(material)
                .orElse(XMaterial.STONE).parseMaterial() ?: Material.STONE
        }
    }

    override fun loadData(data: ItemData): Any {
        return XMaterial.matchXMaterial(data.itemStack).name
    }

}