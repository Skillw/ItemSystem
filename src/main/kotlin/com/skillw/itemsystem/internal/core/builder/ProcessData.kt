package com.skillw.itemsystem.internal.core.builder

import com.skillw.itemsystem.api.builder.IProcessData
import com.skillw.asahi.api.AsahiAPI.analysis
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.context.AsahiContext.Companion.create
import com.skillw.asahi.api.AsahiAPI.asahi
import com.skillw.pouvoir.util.cast
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.platform.util.ItemBuilder

/**
 * @className ProcessData
 *
 * @author Glom
 * @date 2022/8/7 7:30 Copyright 2022 user. All rights reserved.
 */
class ProcessData(override val entity: LivingEntity? = null, val context: AsahiContext = create()) :
    AsahiContext by context, IProcessData {
    override val builder = ItemBuilder(XMaterial.STONE)
    override val nbt = ItemTag()
    override val savingKeys = HashSet<String>()

    //    MM Hook
    init {
        entity?.let { context["entity"] = it }
    }

    constructor(entity: LivingEntity? = null, receiver: ProcessData.() -> Unit) : this(entity) {
        receiver()
    }

    override fun String.eval(): Any {
        return this.asahi(
            namespaces = arrayOf("common", "item_system"),
            context = this@ProcessData
        )
    }

    private fun List<*>.analysis(): List<Any> {
        return this.mapNotNull { it?.analysis() }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> T.analysis(): T {
        return when (this) {
            is String -> analysis(this@ProcessData) as T
            is List<*> -> analysis() as T
            is Map<*, *> -> analysis() as T
            else -> this
        }
    }

    private fun Map<*, *>.analysis(): Map<String, Any> {
        val map = HashMap<String, Any>()
        this.forEach { (keyObj, obj) ->
            val key = keyObj?.analysis()?.toString() ?: return@forEach
            map[key] = when (obj) {

                is List<*> -> {
                    when (obj.firstOrNull()?.toString()?.analysis()?.cast()) {
                        is Byte -> ByteArray(obj.size) { obj[it].toString().analysis().cast() as Byte }
                        is Int -> IntArray(obj.size) { obj[it].toString().analysis().cast() as Int }
                        else -> obj.analysis()
                    }
                }

                is String -> {
                    obj.analysis().cast()
                }

                else -> {
                    obj?.analysis() ?: return@forEach
                }
            }
        }
        return map
    }

    override fun result(receiver: ProcessData.() -> Unit): ItemStack {
        receiver()
        return builder.build().apply item@{
            getItemTag().apply {
                putAll(nbt)
                saveTo(this@item)
            }
        }
    }

    override fun clone(): ProcessData {
        return ProcessData(entity, context.clone())
    }
}
