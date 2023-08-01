package com.skillw.itemsystem.api.meta

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.internal.core.builder.ProcessData
import com.skillw.itemsystem.internal.core.meta.data.MetaData
import com.skillw.pouvoir.api.plugin.map.component.Registrable

/**
 * @className BaseMeta
 *
 * 一个基本的元，用于操作过程数据，以进行物品的构建过程
 *
 * @author Glom
 * @date 2022/8/7 7:20 Copyright 2022 user. All rights reserved.
 */
abstract class BaseMeta(override val key: String) : Registrable<String>, Comparable<BaseMeta> {

    protected open val priority: Int = 1
    open val default: Any? = null

    override fun compareTo(other: BaseMeta): Int = if (this.priority == other.priority) 0
    else if (this.priority > other.priority) 1
    else -1

    /**
     * 调用元
     *
     * @param metaData 元数据
     * @param processData 过程数据
     */
    fun invoke(metaData: MetaData, processData: ProcessData) {
        invoke(Memory(metaData, processData))
    }

    /**
     * 调用元 （内部）
     *
     * @param memory 元记忆，包含元数据和过程数据及各种工具函数
     */
    protected abstract fun invoke(memory: Memory)

    /**
     * 从物品中读取元数据
     *
     * @param data 物品数据
     * @return 元数据
     */
    open fun loadData(data: ItemData): Any? {
        return null
    }


    override fun register() {
        ItemSystem.metaManager.register(this)
    }
}
