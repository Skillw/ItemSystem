package com.skillw.itemsystem.internal.core.option

import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.itemsystem.api.option.BaseOption
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.configuration.ConfigurationSection

@AutoRegister
object OptionLockedLore : BaseOption("locked-lore") {
    override fun init(section: ConfigurationSection, builder: BaseItemBuilder) {
        builder.options["locked-lore"] = section.getBoolean("locked-lore")
    }

    val BaseItemBuilder.lockedLore: Boolean
        get() = options["locked-lore"] as Boolean
}
