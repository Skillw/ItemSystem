package com.skillw.itemsystem.internal.feature.compat.asahi.annotation

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.vartype.VariableType
import com.skillw.itemsystem.internal.manager.ISConfig
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.util.toArgs
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * VarType
 *
 * @constructor VarType Key: String
 */
@AutoRegister
object VarType : ScriptAnnotation("VarType", fileAnnotation = true) {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        if (args.isEmpty()) return
        val key = args[0]
        object : VariableType(key) {
            override fun createVar(memory: Memory): Any {
                return Pouvoir.scriptManager.invoke(script, function, parameters = arrayOf(memory)) ?: "NULL"
            }
        }.register()
        ISConfig.debug { console().sendLang("annotation-var-type-register", key) }
        script.onDeleted("VarType-$key") {
            ISConfig.debug { console().sendLang("annotation-var-type-unregister", key) }
            ItemSystem.varTypeManager.remove(key)
        }
    }
}
