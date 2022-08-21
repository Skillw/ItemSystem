package com.skillw.itemsystem.internal.manager

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.manager.ItemBuilderManager
import com.skillw.itemsystem.internal.builder.ItemBuilder
import com.skillw.itemsystem.util.FileWatcher.unwatch
import com.skillw.itemsystem.util.FileWatcher.watch
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.util.FileUtils.listSubFiles
import com.skillw.pouvoir.util.FileUtils.loadYaml
import com.skillw.pouvoir.util.MapUtils.put
import org.bukkit.configuration.ConfigurationSection
import taboolib.common.platform.function.getDataFolder
import java.io.File

object ItemBuilderManagerImpl : ItemBuilderManager() {
    override val files: Set<File>
        get() = items.keys
    override val key: String = "ItemBuilderManager"
    override val priority: Int = 3
    override val subPouvoir: SubPouvoir = ItemSystem
    private val items = BaseMap<File, HashSet<ItemBuilder>>()

    private fun reloadFile(file: File) {
        items[file]?.forEach { remove(it.key) }
        items.remove(file)
        file.loadYaml()?.apply {
            getKeys(false).forEach { key ->
                val section = this[key] as? ConfigurationSection? ?: return@forEach
                items.put(file, ItemBuilder.deserialize(section).apply { register() })
            }
        }
    }

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
            .forEach { it.watch(this::reloadFile) }
    }
}