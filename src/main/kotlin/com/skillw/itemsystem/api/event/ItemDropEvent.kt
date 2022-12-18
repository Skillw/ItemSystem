package com.skillw.itemsystem.api.event

import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import taboolib.platform.type.BukkitProxyEvent

/**
 * @className ItemDropEvent
 *
 * @author Glom
 * @date 2022/8/26 21:41 Copyright 2022 user. All rights reserved.
 */
class ItemDropEvent(val entity: LivingEntity, val item: Item) : BukkitProxyEvent()