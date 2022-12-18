package com.skillw.itemsystem.api.builder

import com.skillw.itemsystem.internal.core.builder.ProcessData
import com.skillw.itemsystem.internal.core.meta.data.MetaData
import com.skillw.pouvoir.api.able.Registrable
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @className BaseItemBuilder
 *
 * 物品构建器 用于构建物品
 *
 * @author Glom
 * @date 2022/8/15 22:01 Copyright 2022 user. All rights reserved.
 */
abstract class BaseItemBuilder(override val key: String) : Registrable<String>, ConfigurationSerializable {

    abstract val options: MutableMap<String, Any>

    /** 每次更新时，更新的NBT路径 */
//    abstract val lockedNBT: HashSet<String>
//    abstract var autoUpdate: Boolean
    abstract val process: LinkedList<MetaData>

    /**
     * 构建物品
     *
     * @param entity 实体
     * @param processData 过程数据
     * @return 构建出来的物品
     */
    abstract fun build(entity: LivingEntity? = null, processData: ProcessData = ProcessData(entity)): ItemStack

}