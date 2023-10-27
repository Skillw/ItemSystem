package com.skillw.itemsystem.api.event

import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.itemsystem.internal.core.builder.ProcessData
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * @className ItemBuildEvent
 *
 * @author Glom
 * @date 2022/8/15 14:57 Copyright 2022 user. All rights reserved.
 */
class ItemBuildEvent {
    /**
     * 构建物品前，一切流程都没有开始
     *
     * @property builder 物品构建器
     * @property data 过程数据
     * @property entity 实体
     */
    class Pre(val builder: BaseItemBuilder, val data: ProcessData, val entity: LivingEntity?) : BukkitProxyEvent()

    /**
     * 构建物品中，Meta已经运行完毕，下一步就是构建物品
     *
     * @property builder 物品构建器
     * @property data 过程数据
     * @property entity 实体
     */
    class Building(val builder: BaseItemBuilder, val data: ProcessData, val entity: LivingEntity?) : BukkitProxyEvent()

    /**
     * 构建物品后，物品已经构建完毕
     *
     * @property builder 物品构建器
     * @property itemStack 结果物品
     * @property entity 实体
     */
    class Post(
        val builder: BaseItemBuilder,
        val data: ProcessData,
        var itemStack: ItemStack,
        val entity: LivingEntity?,
    ) : BukkitProxyEvent()

    /**
     * 物品更新前，还没有更新，你可以操作物品
     *
     * @property builder 物品构建器
     * @property itemStack 结果物品
     * @property entity 实体
     */
    class Update(
        val builder: BaseItemBuilder,
        val data: ProcessData,
        var itemStack: ItemStack,
        val entity: LivingEntity,
    ) : BukkitProxyEvent()
}
