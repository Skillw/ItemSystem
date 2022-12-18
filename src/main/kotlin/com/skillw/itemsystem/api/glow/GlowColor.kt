package com.skillw.itemsystem.api.glow

import com.skillw.itemsystem.util.nms.NMS
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.map.KeyMap
import org.bukkit.ChatColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.nms.sendPacket
import taboolib.platform.util.bukkitPlugin

/**
 * @className GlowColor
 *
 * @author Glom
 * @date 2022/8/18 9:48 Copyright 2022 user. All rights reserved.
 */
class GlowColor(override val key: ChatColor) : Registrable<ChatColor> {
    private val packet = NMS.INSTANCE.buildColorPacket(key, Operation.CREATE)

    companion object {
        @JvmStatic
        private val glowColors = KeyMap<ChatColor, GlowColor>()

        @JvmStatic
        internal fun Player.initGlowColors() {
            if (hasMetadata("IS_GLOW_SCORE_BOARD") && getMetadata("IS_GLOW_SCORE_BOARD")[0].asString() == player?.scoreboard.toString()) return
            setMetadata("IS_GLOW_SCORE_BOARD", FixedMetadataValue(bukkitPlugin, player?.scoreboard.toString()))
            glowColors.values.map { it.packet }.forEach(::sendPacket)
        }

        @JvmStatic
        internal fun Player.resetGlowColors() {
            glowColors.values.map { it.packet }.forEach(::sendPacket)
        }

        @JvmStatic
        internal fun Player.addGlowEntity(entity: Entity, color: ChatColor) {
            entity.isGlowing = true
            initGlowColors()
            sendPacket(NMS.INSTANCE.buildColorPacket(color, Operation.ADD_ENTITY, entity))
        }

        @JvmStatic
        internal fun Player.removeGlowEntity(entity: Entity, color: ChatColor) {
            entity.isGlowing = false
            initGlowColors()
            sendPacket(NMS.INSTANCE.buildColorPacket(color, Operation.REMOVE_ENTITY, entity))
        }

        @Awake(LifeCycle.ENABLE)
        internal fun initGlowColors() {
            ChatColor.values().map { GlowColor(it) }.forEach(GlowColor::register)
        }
    }

    /**
     * 操作方式
     *
     * @property id 内部id（不用管）
     */
    enum class Operation(val id: Int) {
        /** Create 创建一个Team */
        CREATE(0),

        /** 向队伍添加实体 */
        ADD_ENTITY(3),

        /** 删除队伍实体 */
        REMOVE_ENTITY(4)
    }

    override fun register() {
        glowColors.register(this)
    }
}