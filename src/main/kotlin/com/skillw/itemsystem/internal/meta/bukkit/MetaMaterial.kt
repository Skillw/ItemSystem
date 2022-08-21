package com.skillw.itemsystem.internal.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.taboolib.library.xseries.XMaterial
import org.bukkit.Material
import taboolib.library.xseries.parseToXMaterial

@AutoRegister
object MetaMaterial : BaseMeta("material") {

    override val priority = 2

    override fun invoke(memory: Memory) {
        with(memory) {
            val xMaterial = getString("material").parseToXMaterial()
            builder.material = xMaterial.parseMaterial() ?: Material.STONE
        }
    }

    override fun loadData(data: ItemData): Any {
        return XMaterial.matchXMaterial(data.itemStack).name
    }

}