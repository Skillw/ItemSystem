package com.skillw.itemsystem.internal.feature.listener

import com.skillw.itemsystem.api.ItemAPI.dynamic
import com.skillw.itemsystem.util.nms.NMS
import org.bukkit.GameMode
import org.bukkit.entity.Player
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.PacketSendEvent

private object DynamicListener {
    private val modes = arrayOf(GameMode.SURVIVAL, GameMode.ADVENTURE)

    //XXX: 下板加创造模式发原物品包
    @SubscribeEvent
    fun e(event: PacketSendEvent) {
        if (event.isCancelled) return
        val packet = event.packet
        if (packet.name != "PacketPlayOutWindowItems") return
        val player = event.player
        if (player.gameMode !in modes) return
        NMS.INSTANCE.computeCraftItems(
            player,
            packet.source
        ) { item -> item.dynamic(player) }
    }

    @SubscribeEvent
    fun e2(event: PacketSendEvent) {
        if (event.isCancelled) return
        val packet = event.packet
        if (packet.name != "PacketPlayOutSetSlot") return
        val player = event.player
        if (player.gameMode !in modes) return
        NMS.INSTANCE.computeCraftItem(
            player,
            packet.source
        ) { item -> item.dynamic(player) }
    }
}