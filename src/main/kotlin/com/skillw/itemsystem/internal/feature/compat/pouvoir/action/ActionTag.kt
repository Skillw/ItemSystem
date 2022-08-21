package com.skillw.itemsystem.internal.feature.compat.pouvoir.action

import com.skillw.itemsystem.util.NBTUtils.obj
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData

/**
 * @className ActionTag
 *
 * @author Glom
 * @date 2022/8/19 10:58 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionTag : IAction {
    override val actions: Set<String> =
        hashSetOf("get", "set", "put", "remove", "has")
    override val type: Class<*> = ItemTag::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        if (obj !is ItemTag) error("$obj is not a ItemTag")
        with(parser) {
            with(obj) {
                when (action) {
                    "get" -> {
                        val key = parseString()
                        return key.run { if (contains(".")) getDeep(key) else get(key) }?.obj()
                    }

                    "put" -> {
                        val key = parseString()
                        val value = ItemTagData.toNBT(parseAny())
                        key.run { if (contains(".")) putDeep(key, value) else put(key, value) }
                    }

                    "remove" -> {
                        val key = parseString()
                        key.run { if (contains(".")) removeDeep(key) else remove(key) }
                    }

                    "has" -> {
                        val key = parseString()
                        return obj.containsKey(key)
                    }

                    else -> {
                        error("unknown ItemTag action $action")
                    }
                }
            }
        }
        return null
    }
}