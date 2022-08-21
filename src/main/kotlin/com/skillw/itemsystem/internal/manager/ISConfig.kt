package com.skillw.itemsystem.internal.manager

import com.skillw.itemsystem.ItemSystem
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.ConfigManager
import taboolib.common.platform.Platform
import taboolib.common.platform.function.getDataFolder
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin
import java.io.File

object ISConfig : ConfigManager(ItemSystem) {
    override val priority = 0

    override fun onLoad() {
        createIfNotExists(
            "scripts", "listener/durability.js", "listener/time.js"
        )
        createIfNotExists("items", "Example.yml", "青钢剑.yml")
        createIfNotExists("global", "global.yml")
        Metrics(16051, BukkitPlugin.getInstance().description.version, Platform.BUKKIT).run {

        }
    }

    override fun onEnable() {
        onReload()
    }


    override fun subReload() {
        Pouvoir.scriptManager.addScriptDir(File(getDataFolder(), "scripts"))
    }

    val debug
        get() = this["config"].getBoolean("debug")

    val listSection
        get() = this["config"].getConfigurationSection("list")

    val ignoreNbtKeysOnSaving: List<String>
        get() = this["config"].getStringList("options.save-ignore-nbt-keys")

    val threadPoolSize: Int
        get() = this["config"].getInt("options.thread-pool-size")

    @JvmStatic
    fun debug(debug: () -> Unit) {
        if (this.debug) {
            debug.invoke()
        }
    }
}