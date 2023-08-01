package com.skillw.itemsystem.internal.core.option

import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.itemsystem.api.option.BaseOption
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.configuration.ConfigurationSection

@AutoRegister
object OptionLockedNBTKeys : BaseOption("locked-nbt-keys") {
    override fun init(section: ConfigurationSection, builder: BaseItemBuilder) {
        builder.options["locked-nbt-keys"] = section.getStringList("locked-nbt-keys")
    }

    val BaseItemBuilder.lockedNBT: List<String>
        get() = options["locked-nbt-keys"] as List<String>
}
