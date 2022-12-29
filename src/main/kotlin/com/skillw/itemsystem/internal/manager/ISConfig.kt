package com.skillw.itemsystem.internal.manager

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.ItemAPI
import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.ConfigManager
import com.skillw.pouvoir.util.ClassUtils.static
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.Platform
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.module.chat.colored
import taboolib.module.lang.asLangText
import taboolib.module.metrics.Metrics
import taboolib.module.nms.ItemTag
import taboolib.platform.BukkitPlugin
import java.io.File
import java.util.function.Function

object ISConfig : ConfigManager(ItemSystem) {
    override val priority = 0

    val debug
        get() = this["config"].getBoolean("debug")

    val listSection
        get() = this["config"].getConfigurationSection("list")

    val threadPoolSize: Int
        get() = this["config"].getInt("options.thread-pool-size")

    val glowDrops: Boolean
        get() = this["config"].getBoolean("options.drops-glow", true)

    var unknownDynamic: String = ""

    override fun onLoad() {
        createIfNotExists(
            "scripts", "durability.js", "time.js", "action.js", "custom.js", "gem.js"
        )
        createIfNotExists("items", "action.yml", "example.yml", "gem.yml")
        createIfNotExists("global", "global.yml")
        Metrics(16051, BukkitPlugin.getInstance().description.version, Platform.BUKKIT).run {

        }
        Pouvoir.scriptEngineManager.globalVariables.also {
            it["ItemAPI"] = ItemAPI::class.java.static()
            it["getTag"] = Function<ItemStack,ItemTag>{ item ->
                return@Function item.getTag()
            }
        }
    }

    override fun onEnable() {
        onReload()
    }

    override fun subReload() {
        Pouvoir.scriptManager.addScriptDir(File(getDataFolder(), "scripts"))
        unknownDynamic = console().asLangText("unknown-dynamic").colored()
    }

    @JvmStatic
    fun debug(debug: () -> Unit) {
        if (this.debug) {
            debug.invoke()
        }
    }

}