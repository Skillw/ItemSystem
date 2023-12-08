package com.skillw.itemsystem.internal.core.meta.data

import com.skillw.itemsystem.ItemSystem.globalManager
import com.skillw.itemsystem.ItemSystem.metaManager
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.IMetaData
import com.skillw.itemsystem.internal.core.meta.define.MetaDefine
import com.skillw.pouvoir.api.plugin.map.BaseMap


/**
 * @className MetaModelData
 *
 * @author Glom
 * @date 2022/8/7 7:26 Copyright 2022 user. All rights reserved.
 */
class MetaData(override val key: BaseMeta) : BaseMap<String, Any>(), IMetaData {

    companion object {
        @JvmStatic
        fun deserialize(data: Map<String, Any>): MetaData {
            return when (data.size) {
                1 -> {
                    val metaKey = data.keys.first()
                    if (metaKey.equals("global", true)) {
                        return globalManager[data[metaKey].toString()]
                            ?: error("Unknown Global MetaData ${data[metaKey]}")
                    }
                    val meta = metaManager[data.keys.first()] ?: MetaDefine
                    MetaData(meta).apply { putAll(data) }
                }

                else -> {
                    val metaKey = data["meta"].toString()
                    val meta = metaManager[metaKey] ?: MetaDefine
                    MetaData(meta).apply { putAll(data) }
                }
            }
        }
    }

    override fun serialize(): MutableMap<String, Any> {
        return toMutableMap()
    }


    override fun clone(): MetaData {
        return MetaData(key).apply { putAll(this) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MetaData) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode() + toMutableMap().hashCode()
    }
}
