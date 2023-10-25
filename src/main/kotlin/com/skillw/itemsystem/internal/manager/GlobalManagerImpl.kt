package com.skillw.itemsystem.internal.manager

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.ItemSystem.itemBuilderManager
import com.skillw.itemsystem.api.manager.GlobalManager
import com.skillw.itemsystem.internal.core.meta.data.MetaData
import com.skillw.itemsystem.util.FileWatcher.unwatch
import com.skillw.itemsystem.util.FileWatcher.watch
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.util.listSubFiles
import com.skillw.pouvoir.util.loadYaml
import com.skillw.pouvoir.util.toMap
import org.bukkit.configuration.ConfigurationSection
import taboolib.common.platform.function.getDataFolder
import java.io.File

object GlobalManagerImpl : GlobalManager() {
    private fun readResolve(): Any = GlobalManagerImpl

    override val key: String = "GlobalManager"
    override val priority: Int = 2
    override val subPouvoir: SubPouvoir = ItemSystem
    private val globals = BaseMap<File, HashSet<String>>()

    private fun reload(file: File) {
        globals[file]?.forEach(this::remove)
        globals.remove(file)
        file.loadYaml()?.apply {
            getKeys(false).forEach { key ->
                val section = this[key] as? ConfigurationSection? ?: return@forEach
                register(key, MetaData.deserialize(section.toMap()))
            }
        }
    }

    private fun fileReload(file: File) {
        reload(file)
        itemBuilderManager.onReload()
    }

    override fun onEnable() {
        onReload()
    }

    override fun clear() {
        globals.keys.forEach { it.unwatch() }
        globals.clear()
        super.clear()
    }

    override fun onReload() {
        clear()
        File(getDataFolder(), "global").listSubFiles()
            .filter { it.extension == "yml" }
            .forEach { it.watch(this::fileReload); reload(it) }
    }
}
