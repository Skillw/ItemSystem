package com.skillw.itemsystem.internal.feature.compat.pouvoir.annotation

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.internal.manager.ISConfig
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.util.StringUtils.toArgs
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * Meta 读取格式注解 (是文件注解 请在脚本文件第一行写)
 *
 * @constructor Meta Key: String
 */
@AutoRegister
object Meta : ScriptAnnotation("Meta", fileAnnotation = true) {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        if (function != "null") return
        if (args.isEmpty()) return
        val key = args[0]
        object : BaseMeta(key) {
            override fun invoke(memory: Memory) {
                Pouvoir.scriptManager.invoke<Any?>(script, "process", parameters = arrayOf(memory))
            }

            override fun loadData(data: ItemData): Any? {
                return Pouvoir.scriptManager.invoke(script, "loadData", parameters = arrayOf(data))
            }
        }.register()
        ISConfig.debug { console().sendLang("annotation-meta-register", key) }
        script.onDeleted("Meta-$key") {
            ISConfig.debug { console().sendLang("annotation-meta-unregister", key) }
            ItemSystem.metaManager.remove(key)
        }
    }
}