package com.skillw.itemsystem.internal.feature.compat.asahi.infix

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.itemsystem.util.NBTUtils.obj
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData

/**
 * @className ActionTag
 *
 * @author Glom
 * @date 2022/8/19 10:58 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
object InfixTag : BaseInfix<ItemTag>(ItemTag::class.java) {
    init {
        infix("get") { tag ->
            val key = parseString()
            key.run { if (contains(".")) tag.getDeep(key) else tag[key] }?.obj()
        }

        infix("put") { tag ->
            val key = parseString()
            expect("to")
            val value = ItemTagData.toNBT(parseAny())
            key.run { if (contains(".")) tag.putDeep(key, value) else tag.put(key, value) }?.obj()
        }

        infix("remove") { tag ->
            val key = parseString()
            key.run { if (contains(".")) tag.removeDeep(key) else tag.remove(key) }?.obj()
        }

        infix("has") { tag ->
            val key = parseString()
            key.run { if (contains(".")) tag.getDeep(key) != null else tag.containsKey(key) }
        }

        infix("saveTo") { tag ->
            val item = parse<ItemStack>()
            tag.saveTo(item)
        }
    }
}
