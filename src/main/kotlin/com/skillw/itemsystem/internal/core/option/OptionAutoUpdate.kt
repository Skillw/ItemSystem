package com.skillw.itemsystem.internal.core.option

import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.itemsystem.api.option.BaseOption
import com.skillw.pouvoir.api.annotation.AutoRegister
import org.bukkit.configuration.ConfigurationSection

@AutoRegister
object OptionAutoUpdate : BaseOption("auto-update") {
    override fun init(section: ConfigurationSection, builder: BaseItemBuilder) {
        builder.options["auto-update"] = section.getBoolean("auto-update", false)
    }

    val BaseItemBuilder.autoUpdate: Boolean
        get() = options["auto-update"].toString().toBoolean()
}