package com.skillw.itemsystem.internal.core.meta.bukkit.interact

import com.skillw.itemsystem.api.ItemAPI.fromIS
import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.internal.feature.ItemCache.cacheTag
import com.skillw.itemsystem.util.NBTUtils.obj
import com.skillw.pouvoir.api.annotation.AutoRegister
import org.bukkit.event.inventory.CraftItemEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTagData
import taboolib.platform.util.isNotAir

@AutoRegister
object MetaCanCraft : BaseMeta("can-craft") {
    override val priority = 3
    override val default = "false"

    override fun invoke(memory: Memory) {
        with(memory) {
            nbt["ITEM_SYSTEM.can-craft"] = ItemTagData.toNBT(getString("can-craft"))
        }
    }

    override fun loadData(data: ItemData): Any {
        return data.itemTag.getOrDefault("ITEM_SYSTEM.can-craft", ItemTagData("false")).obj()
            .also { data.itemTag.remove("ITEM_SYSTEM.can-craft") }
    }

    @SubscribeEvent
    fun craft(event: CraftItemEvent) {
        if (event.inventory.matrix.any {
                it.isNotAir() &&
                        it.cacheTag().getOrDefault("ITEM_SYSTEM.can-craft", ItemTagData("true"))?.asString() == "false"
            } && !event.recipe.result.fromIS()) {
            event.isCancelled = true
        }
    }

}