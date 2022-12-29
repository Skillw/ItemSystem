package com.skillw.itemsystem.api

import com.skillw.itemsystem.api.glow.GlowColor.Companion.addGlowEntity
import com.skillw.itemsystem.api.glow.GlowColor.Companion.removeGlowEntity
import org.bukkit.ChatColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object GlowAPI {
    /**
     * 给玩家添加发光实体
     *
     * @param entity Entity 实体
     * @param color ChatColor 颜色
     * @receiver Player 玩家
     */
    @JvmStatic
    fun Player.addGlow(entity: Entity, color: ChatColor) {
        addGlowEntity(entity, color)
    }

    /**
     * 给玩家删除发光实体
     *
     * @param entity Entity 实体
     * @param color ChatColor 颜色
     * @receiver Player 玩家
     */
    @JvmStatic
    fun Player.removeGlow(entity: Entity, color: ChatColor) {
        removeGlowEntity(entity, color)
    }
}