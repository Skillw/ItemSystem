package com.skillw.itemsystem.api.manager

import com.skillw.itemsystem.api.action.ActionType
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.LowerKeyMap
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

/**
 * @className ActionTypeManager
 *
 * 用于维护所有的 物品动作类型
 *
 * @author Glom
 * @date 2022年10月23日 10:02:44 Copyright 2022 user. All rights reserved.
 */
abstract class ActionTypeManager : Manager, LowerKeyMap<ActionType>() {
    abstract fun call(
        type: ActionType,
        entity: LivingEntity,
        item: ItemStack,
        receiver: MutableMap<String, Any>.() -> Unit,
    )

    abstract fun call(
        string: String,
        entity: LivingEntity,
        itemStack: ItemStack,
        receiver: MutableMap<String, Any>.() -> Unit,
    )
}