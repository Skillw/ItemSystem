package com.skillw.itemsystem.util

import org.bukkit.ChatColor
import taboolib.common5.Coerce

object ColorUtils {
    @JvmStatic
    fun String.toColor(): ChatColor {
        return Coerce.toEnum(this, ChatColor::class.java, ChatColor.WHITE)
    }
}