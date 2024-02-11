package com.skillw.itemsystem.util.nms

import com.skillw.itemsystem.api.glow.GlowColor
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsProxy

/**
 * @className NMS
 *
 * @author Glom
 * @date 2022/8/9 22:24 Copyright 2022 user. All rights reserved.
 */
abstract class NMS {
    val itemsField by unsafeLazy {
        if (MinecraftVersion.major >= 9) "c"
        else "b"
    }
    val itemField by unsafeLazy {
        if (MinecraftVersion.major >= 9) "f"
        else "c"
    }

    enum class VersionType {
        LEGACY, AQUATIC, UNIVERSAL
    }

    protected val versionType = when (MinecraftVersion.major) {
        in 0..4 -> VersionType.LEGACY
        in 5..8 -> VersionType.AQUATIC
        else -> VersionType.UNIVERSAL
    }
    protected val nameField = when (versionType) {
        VersionType.LEGACY, VersionType.AQUATIC -> "a"
        else -> "i"
    }
    protected val typeField = when (versionType) {
        VersionType.LEGACY, VersionType.AQUATIC -> "i"
        else -> "h"
    }
    protected val entitiesField = when (versionType) {
        VersionType.LEGACY, VersionType.AQUATIC -> "h"
        else -> "j"
    }

    abstract fun computeCraftItems(player: Player, packet: Any, func: (ItemStack) -> Unit)
    abstract fun computeCraftItem(player: Player, packet: Any, func: (ItemStack) -> Unit)
    abstract fun buildColorPacket(chatColor: ChatColor, type: GlowColor.Operation, entity: Entity? = null): Any
    abstract fun getEntity(world: World, id: Int): Entity?


    companion object {
        @JvmStatic
        val INSTANCE by lazy {
            nmsProxy<NMS>()
        }
    }

}