package com.skillw.itemsystem.internal.feature.listener

import com.skillw.itemsystem.api.ItemAPI.replace
import com.skillw.itemsystem.util.nms.NMS
import org.bukkit.GameMode
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.PacketSendEvent

private object SyncListener {
    private val modes = arrayOf(GameMode.SURVIVAL, GameMode.ADVENTURE)

    @SubscribeEvent
    fun e(event: PacketSendEvent) {
        if (event.isCancelled) return
        val packet = event.packet
        if (packet.name != "PacketPlayOutWindowItems") return
        val player = event.player
        if (player.gameMode !in modes) return
        NMS.INSTANCE.computeCraftItems(player, packet.source) { item -> item.replace(player) }
    }

    @SubscribeEvent
    fun e2(event: PacketSendEvent) {
        if (event.isCancelled) return
        val packet = event.packet
        if (packet.name != "PacketPlayOutSetSlot") return
        val player = event.player
        if (player.gameMode !in modes) return
        NMS.INSTANCE.computeCraftItem(player, packet.source) { item -> item.replace(player) }
    }
}