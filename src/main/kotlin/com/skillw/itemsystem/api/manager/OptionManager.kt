package com.skillw.itemsystem.api.manager

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.itemsystem.api.option.BaseOption
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import org.bukkit.configuration.ConfigurationSection

/**
 * @className MetaManager
 *
 * 用于维护所有的 BaseOption (元)
 *
 * @author Glom
 * @date 2022/10/7 19:39 Copyright 2022 user. All rights reserved.
 */
object OptionManager : Manager, KeyMap<String, BaseOption>() {
    override val key: String = "OptionManager"
    override val priority: Int = 1
    override val subPouvoir: SubPouvoir = ItemSystem

    fun initOption(section: ConfigurationSection, builder: BaseItemBuilder) {
        values.forEach {
            it.init(section, builder)
        }
    }
}