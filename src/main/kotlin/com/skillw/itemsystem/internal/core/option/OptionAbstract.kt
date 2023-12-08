package com.skillw.itemsystem.internal.core.option

import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.itemsystem.api.option.BaseOption
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.configuration.ConfigurationSection

@AutoRegister
object OptionAbstract : BaseOption("abstract") {
    override fun init(section: ConfigurationSection, builder: BaseItemBuilder) {
        builder.options["abstract"] = section.getBoolean("abstract")
    }

    val BaseItemBuilder.abstract: Boolean
        get() = options["abstract"] as Boolean
}
