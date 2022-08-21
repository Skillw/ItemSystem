package com.skillw.itemsystem.internal.feature.listener

import com.skillw.itemsystem.internal.feature.ItemUpdater.updateItem
import com.skillw.itemsystem.internal.feature.ItemUpdater.updateItems
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.player.PlayerRespawnEvent
import taboolib.common.platform.Schedule
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.OptionalEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

private object UpdateListener {
    @Schedule(period = 100, async = true)
    fun tick() {
        Bukkit.getOnlinePlayers().forEach { player -> player.updateItems() }
    }

    @SubscribeEvent
    fun onJoin(e: PlayerJoinEvent) {
        e.player.updateItems()
    }

    @SubscribeEvent
    fun onRespawn(e: PlayerRespawnEvent) {
        e.player.updateItems()
    }

    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDrop(e: PlayerDropItemEvent) {
        e.itemDrop.itemStack = e.itemDrop.itemStack.updateItem(e.player)
    }

    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPickup(e: PlayerPickupItemEvent) {
        e.item.itemStack = e.item.itemStack.updateItem(e.player)
    }

    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onOpen(e: InventoryOpenEvent) {
        kotlin.runCatching {
            if (e.inventory.location != null) {
                submit(async = true) {
                    (e.player as Player).updateItems()
                }
            }
        }
    }

    @SubscribeEvent(bind = "cc.bukkitPlugin.pds.events.PlayerDataLoadCompleteEvent")
    fun onLoad(e: OptionalEvent) {
        val player = e.read<Player>("player")!!
        player.updateItems()
    }
}