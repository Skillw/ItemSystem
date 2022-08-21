package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.itemsystem.api.event.ItemBuildEvent
import com.skillw.itemsystem.internal.feature.ItemSyncer.syncPattern
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.uncolored
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagList
import taboolib.module.nms.getItemTag
import taboolib.platform.util.hasLore

@AutoRegister
object FunctionSyncNBT : PouFunction<String>("syncNBT", namespace = "item_system") {

    @SubscribeEvent
    fun after(event: ItemBuildEvent.After) {
        val item = event.itemStack
        val tag = item.getItemTag()
        if (!item.hasLore()) return
        val set = HashSet<String>()
        item.itemMeta.lore?.forEachIndexed { index, lore ->
            val matcher = syncPattern.matcher(lore.uncolored())
            while (matcher.find()) {
                val nbt = matcher.group("nbt")
                set.add(nbt)
            }
        }
        tag["ITEM_SYSTEM"]!!.asCompound().getOrPut("SYNC_KEYS") { ItemTagList() }.asList()
            .addAll(ItemTagData.toNBT(set.mapNotNull { ItemTagData.toNBT(it) }).asList())
        tag.saveTo(item)
    }

    override fun execute(parser: Parser): String {
        with(parser) {
            val nbt = parseString()
            return "_sync_${nbt}_"
        }
    }
}