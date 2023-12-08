package com.skillw.itemsystem

import com.skillw.itemsystem.api.manager.*
import com.skillw.itemsystem.internal.manager.ISConfig
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.annotation.PouManager
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.platform.BukkitPlugin

object ItemSystem : Plugin(), SubPouvoir {

    /** Basic */

    override val key = "ItemSystem"
    override val plugin: JavaPlugin by lazy {
        BukkitPlugin.getInstance()
    }

    /** Config */
    @Config("config.yml")
    lateinit var config: ConfigFile

    /** Managers */

    override lateinit var managerData: ManagerData

    @JvmStatic
    @PouManager
    lateinit var configManager: ISConfig

    @JvmStatic
    @PouManager
    lateinit var optionManager: OptionManager

    @JvmStatic
    @PouManager
    lateinit var metaManager: MetaManager

    @JvmStatic
    @PouManager
    lateinit var itemBuilderManager: ItemBuilderManager

    @JvmStatic
    @PouManager
    lateinit var globalManager: GlobalManager

    @JvmStatic
    @PouManager
    lateinit var varTypeManager: VarTypeManager

    @JvmStatic
    @PouManager
    lateinit var actionTypeManager: ActionTypeManager

    override fun onLoad() {
        load()
    }

    override fun onEnable() {
        enable()
    }

    override fun onActive() {
        active()
    }

    override fun onDisable() {
        disable()
    }

}
