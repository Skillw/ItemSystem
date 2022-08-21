package com.skillw.itemsystem.api.meta.data

import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.internal.meta.data.MetaData
import com.skillw.pouvoir.api.able.Keyable
import org.bukkit.configuration.serialization.ConfigurationSerializable

/**
 * IMetaData
 *
 * 原数据接口
 *
 * @constructor Create empty I meta data
 */
interface IMetaData : Keyable<BaseMeta>, ConfigurationSerializable {
    /**
     * 克隆元数据
     *
     * @return 元数据
     */
    fun clone(): MetaData
}