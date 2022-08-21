package com.skillw.itemsystem.internal.feature.listener

import com.skillw.itemsystem.api.action.ActionType
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.isAir

private object ActionListener {

    @SubscribeEvent(EventPriority.LOWEST)
    fun interact(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand
        if (item.isAir()) return
        with(event) {
            val left = action in arrayOf(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK)
            val right = action in arrayOf(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)
            val shift = player.isSneaking
            when {
                shift && left -> ActionType.call("shift_left", player, item) {}
                shift && right -> ActionType.call("shift_right", player, item) {}
                left -> ActionType.call("left", player, item) {}
                right -> ActionType.call("right", player, item) {}
            }
        }
    }

    @SubscribeEvent
    fun click(event: InventoryClickEvent) {
        event.clickedInventory ?: return
        if (event.click != ClickType.LEFT) return
        val player = event.whoClicked as? org.bukkit.entity.Player ?: return
        val cursor = event.cursor ?: return
        val current = event.currentItem ?: return
        if (cursor.isAir() || current.isAir) return
        ActionType.call("click_item", player, cursor) {
            put("cursor", cursor)
            put("current", current)
        }
    }

}