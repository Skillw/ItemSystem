package com.skillw.itemsystem.internal.feature.listener

import com.skillw.itemsystem.internal.feature.ItemSyncer.syncNBT
import com.skillw.itemsystem.util.nms.NMS
import org.bukkit.GameMode
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.PacketSendEvent

private object SyncListener {
    @SubscribeEvent
    fun e(event: PacketSendEvent) {
        val packet = event.packet
        if (packet.name != "PacketPlayOutWindowItems") return
        val player = event.player
        if (player.gameMode in arrayOf(GameMode.SURVIVAL, GameMode.ADVENTURE))
            NMS.INSTANCE.computeCraftItems(player, packet.source) { item -> item.syncNBT(player) }
    }

    @SubscribeEvent
    fun e2(event: PacketSendEvent) {
        val packet = event.packet
        if (packet.name != "PacketPlayOutSetSlot") return
        val player = event.player
        if (player.gameMode in arrayOf(GameMode.SURVIVAL, GameMode.ADVENTURE))
            NMS.INSTANCE.computeCraftItem(player, packet.source) { item -> item.syncNBT(player) }
    }
}