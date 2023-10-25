package com.skillw.itemsystem.internal.manager

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.manager.ItemBuilderManager
import com.skillw.itemsystem.internal.core.builder.ItemBuilder
import com.skillw.itemsystem.util.FileWatcher.unwatch
import com.skillw.itemsystem.util.FileWatcher.watch
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.util.listSubFiles
import com.skillw.pouvoir.util.loadYaml
import com.skillw.pouvoir.util.put
import org.bukkit.configuration.ConfigurationSection
import taboolib.common.platform.function.getDataFolder
import java.io.File

object ItemBuilderManagerImpl : ItemBuilderManager() {
    private fun readResolve(): Any = ItemBuilderManagerImpl

    override val files: Set<File>
        get() = items.keys
    override val key: String = "ItemBuilderManager"
    override val priority: Int = 3
    override val subPouvoir: SubPouvoir = ItemSystem
    override val loading: MutableSet<ConfigurationSection> = HashSet()
    private val items = BaseMap<File, HashSet<ItemBuilder>>()

    override fun onEnable() {
        onReload()
    }

    override fun clear() {
        items.keys.forEach { it.unwatch() }
        items.clear()
        super.clear()
    }

    override fun onReload() {
        clear()
        File(getDataFolder(), "items").listSubFiles()
            .filter { it.extension == "yml" }
            .forEach {
                it.watch { reload() }
                items[it] = HashSet()
            }
        reload()
    }

    private fun reload() {
        reloadItems()
        var count = 0
        while (loading.isNotEmpty()) {
            reloadItems()
            if (count++ >= 10) break
        }
    }

    private fun reloadItems() {
        items.keys.forEach { file ->
            file.loadYaml()?.apply {
                getKeys(false).forEach inner@{ key ->
                    val section = this[key] as? ConfigurationSection? ?: return@inner
                    items.put(file, ItemBuilder.deserialize(section).apply { register() })
                }
            }
        }
    }
}
