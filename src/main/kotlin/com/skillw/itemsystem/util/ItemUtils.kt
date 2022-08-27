package com.skillw.itemsystem.util

import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.getName


/**
 * Item utils 给脚本用的
 *
 * @constructor Create empty Item utils
 */

object ItemUtils {

    @ScriptTopLevel
    @JvmStatic
    fun name(item: ItemStack): String {
        return item.getName()
    }

}