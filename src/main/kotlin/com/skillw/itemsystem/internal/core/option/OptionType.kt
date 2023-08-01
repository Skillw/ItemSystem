package com.skillw.itemsystem.internal.core.option

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.itemsystem.api.option.BaseOption
import com.skillw.pouvoir.api.plugin.ManagerTime
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.configuration.ConfigurationSection

@AutoRegister
object OptionType : BaseOption("type") {
    val types: HashSet<String> by lazy {
        ItemSystem.optionManager.addExec("type-clear", ManagerTime.BEFORE_RELOAD) {
            OptionType.types.clear()
        }
        HashSet()
    }

    override fun init(section: ConfigurationSection, builder: BaseItemBuilder) {
        section.getString("type")?.also {
            types.add(it)
            builder.options["type"] = it
        }
    }

    val BaseItemBuilder.type: String
        get() = options["type"]?.toString() ?: "default"
}
