package com.skillw.itemsystem.internal.core.meta.bukkit.interact

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.event.ItemBuildEvent
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.itemsystem.internal.feature.ItemDrop
import com.skillw.itemsystem.internal.feature.ItemDrop.drop
import com.skillw.itemsystem.internal.feature.block.BlockData
import com.skillw.itemsystem.util.NBTUtils.obj
import com.skillw.pouvoir.api.annotation.AutoRegister
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getName
import taboolib.module.nms.setItemTag
import taboolib.platform.util.sendLang

@AutoRegister
object MetaCanBePlaced : BaseMeta("can-be-placed") {
    override val priority = 3
    override val default = "true"
    override fun invoke(memory: Memory) {
        with(memory) {
            nbt["ITEM_SYSTEM.can-be-placed"] = ItemTagData.toNBT(getString("can-be-placed"))
        }
    }

    override fun loadData(data: ItemData): Any {
        return data.itemTag.getOrDefault("ITEM_SYSTEM.can-be-placed", ItemTagData("true")).obj()
            .also { data.itemTag.remove("ITEM_SYSTEM.can-be-placed") }
    }

    @SubscribeEvent
    fun build(event: ItemBuildEvent.After) {
        event.itemStack.apply {
            setItemTag(getTag().also {
                it["ITEM_SYSTEM"]?.asCompound()?.putIfAbsent("ITEM_SYSTEM.can-be-placed", ItemTagData("true"))
            })
        }
    }

    @SubscribeEvent
    fun build(event: BlockPlaceEvent) {
        val player = event.player
        val blockItem = event.itemInHand
        val nbt = blockItem.getTag()
        val canBuild = nbt["ITEM_SYSTEM.can-be-placed"]?.asString()?.toBoolean() ?: return
        if (!canBuild) {
            player.sendLang("item-cant-be-built", blockItem.getName())
            event.isCancelled = true
            return
        }
        val location = event.block.location.toBlockLocation()
        BlockData.push(location, blockItem)
    }

    @SubscribeEvent
    fun dig(event: BlockBreakEvent) {
        val player = event.player
        val location = event.block.location.toBlockLocation()
        BlockData.pull(location, player)?.let {
            event.isDropItems = false
            it.drop(location, ItemDrop.DropData(player))
        }
    }

}