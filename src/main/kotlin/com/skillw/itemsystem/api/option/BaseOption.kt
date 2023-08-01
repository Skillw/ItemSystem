package com.skillw.itemsystem.api.option

import com.skillw.itemsystem.ItemSystem.optionManager
import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import org.bukkit.configuration.ConfigurationSection

/**
 * @className BaseOption
 *
 * 物品选项 请不要把"process"当作物品选项key
 *
 * @author Glom
 * @date 2022/10/7 19:38 Copyright 2022 user. All rights reserved.
 */
abstract class BaseOption(override val key: String) : Registrable<String> {

    /**
     * 初始化物品选项
     *
     * @param section ConfigurationSection 物品构建器的配置节点
     * @param builder BaseItemBuilder 正在初始化的物品构建器
     */
    abstract fun init(section: ConfigurationSection, builder: BaseItemBuilder)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseOption) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun register() {
        optionManager.register(key, this)
    }

}
