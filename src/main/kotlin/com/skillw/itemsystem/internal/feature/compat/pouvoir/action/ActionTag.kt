package com.skillw.itemsystem.internal.feature.compat.pouvoir.action

import com.skillw.itemsystem.util.NBTUtils.obj
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData

/**
 * @className ActionTag
 *
 * @author Glom
 * @date 2022/8/19 10:58 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionTag : PouAction<ItemTag>(ItemTag::class.java) {
    init {
        addExec("get") { tag ->
            val key = parseString()
            key.run { if (contains(".")) tag.getDeep(key) else tag[key] }?.obj()
        }

        addExec("put") { tag ->
            val key = parseString()
            except("to")
            val value = ItemTagData.toNBT(parseAny())
            key.run { if (contains(".")) tag.putDeep(key, value) else tag.put(key, value) }?.obj()
        }

        addExec("remove") { tag ->
            val key = parseString()
            key.run { if (contains(".")) tag.removeDeep(key) else tag.remove(key) }?.obj()
        }

        addExec("has") { tag ->
            val key = parseString()
            key.run { if (contains(".")) tag.getDeep(key) != null else tag.containsKey(key) }
        }

        addExec("saveTo") { tag ->
            val item = parse<ItemStack>()
            tag.saveTo(item)
        }
    }
}