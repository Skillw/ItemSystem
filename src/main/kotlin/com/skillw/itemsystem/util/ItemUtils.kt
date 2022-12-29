package com.skillw.itemsystem.util

import com.skillw.itemsystem.api.ItemAPI.dynamic
import com.skillw.itemsystem.internal.feature.ItemCache.cacheLore

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.module.chat.TellrawJson
import taboolib.module.chat.colored
import taboolib.module.lang.asLangText
import taboolib.module.nms.getName
import taboolib.platform.util.ItemBuilder


/**
 * Item utils 给脚本用的
 *
 * @constructor Create empty Item utils
 */

object ItemUtils {

    @JvmStatic
    fun TellrawJson.displayItem(item: ItemStack?): TellrawJson {
        val text = item?.run {
            buildList {
                add(("&9" + getName()).colored())
                addAll(cacheLore().colored())
            }.joinToString("\n")
        } ?: console().asLangText("command-list-error-item")
        
        return hoverText(text)
    }

    
    @JvmStatic
    fun ItemStack.displayClone(entity: LivingEntity? = null): ItemStack {
        return clone().apply { entity?.let { dynamic(it) } }.let {
            ItemBuilder(it.type).apply {
                name = it.getName(entity as? Player?)
                lore.addAll(it.cacheLore())
            }.build()
        }
    }

    
    @JvmStatic
    fun name(item: ItemStack): String {
        return item.getName()
    }

}