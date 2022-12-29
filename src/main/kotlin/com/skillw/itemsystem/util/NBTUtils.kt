package com.skillw.itemsystem.util


import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagList
import taboolib.module.nms.ItemTagType

/**
 * @className NBTUtils
 *
 * @author Glom
 * @date 2022/8/14 9:03 Copyright 2022 user. All rights reserved.
 */
object NBTUtils {
    @JvmStatic
    fun ItemTag.toMutableMap(strList: List<String> = emptyList()): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        for (it in this) {
            val key = it.key
            if (strList.contains(key)) continue
            val value = it.value.obj()
            map[key] = value
        }
        return map
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    
    @JvmStatic
    fun ItemTagData.obj(): Any {
        val value = when (this.type) {
            ItemTagType.BYTE -> this.asByte()
            ItemTagType.SHORT -> this.asShort()
            ItemTagType.INT -> this.asInt()
            ItemTagType.LONG -> this.asLong()
            ItemTagType.FLOAT -> this.asFloat()
            ItemTagType.DOUBLE -> this.asDouble()
            ItemTagType.STRING -> this.asString()
            ItemTagType.BYTE_ARRAY -> this.asByteArray()
            ItemTagType.INT_ARRAY -> this.asIntArray()
            ItemTagType.COMPOUND -> this.asCompound()
            ItemTagType.LIST -> this.asList()
            else -> this.asString()
        }
        return when (value) {
            is ItemTag -> {
                value.toMutableMap()
            }

            is ItemTagList -> {
                val list = ArrayList<Any>()
                value.forEach {
                    list.add(it.obj())
                }
                list
            }

            else -> value
        }
    }

    
    @JvmStatic
    fun toNBT(any: Any): ItemTagData {
        return ItemTagData.toNBT(any)
    }
}