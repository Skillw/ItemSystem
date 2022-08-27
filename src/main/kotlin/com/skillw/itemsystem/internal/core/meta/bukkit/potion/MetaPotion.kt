package com.skillw.itemsystem.internal.core.meta.bukkit.potion

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType
import taboolib.library.xseries.XPotion

@AutoRegister
object MetaPotion : BaseMeta("potion") {

    override val priority = 9

    override fun invoke(memory: Memory) {
        with(memory) {
            val potionType =
                XPotion.matchXPotion(getString("type", "WATER")).run { if (isPresent) get().potionType else null }
                    ?: PotionType.WATER
            builder.potionData = PotionData(potionType, getBoolean("extended", false), getBoolean("upgraded", false))
        }
    }

    override fun loadData(data: ItemData): Any? {
        data.itemTag.remove("Potion")
        val potionData = data.builder.potionData ?: return null
        return linkedMapOf(
            "type" to potionData.type.name,
            "extended" to potionData.isExtended,
            "upgraded" to potionData.isUpgraded
        )
    }

}