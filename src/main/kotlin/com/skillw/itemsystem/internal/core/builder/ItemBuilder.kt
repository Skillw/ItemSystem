package com.skillw.itemsystem.internal.core.builder

import com.skillw.itemsystem.ItemSystem.itemBuilderManager
import com.skillw.itemsystem.ItemSystem.metaManager
import com.skillw.itemsystem.ItemSystem.optionManager
import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.event.ItemBuildEvent
import com.skillw.itemsystem.internal.core.meta.data.MetaData
import com.skillw.itemsystem.internal.core.option.OptionAbstract.abstract
import com.skillw.itemsystem.internal.core.option.OptionType.type
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common5.mirrorNow
import taboolib.module.lang.asLangText
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import java.util.*
import java.util.function.Supplier

/**
 * @className ItemBuilder
 *
 * @author Glom
 * @date 2022/8/7 7:19 Copyright 2022 user. All rights reserved.
 */
class ItemBuilder(key: String) : BaseItemBuilder(key) {

    override val options = HashMap<String, Any>()
    override val process = LinkedList<MetaData>()

    override fun build(entity: LivingEntity?, processData: ProcessData): ItemStack {
        if (abstract) {
            return taboolib.platform.util.ItemBuilder(Material.STONE).apply {
                name = console().asLangText("command-list-abstract-item")
            }.build()
        }
        val pre = ItemBuildEvent.Pre(this, processData, entity)
        pre.call()
        var data = pre.data
        return mirrorNow("product") {
            process.forEach { metaData ->
                metaData.key.invoke(metaData, data)
            }
            val building = ItemBuildEvent.Building(this, data, entity)
            building.call()
            data = building.data
            return@mirrorNow data.result {
                nbt.getOrPut("ITEM_SYSTEM") { ItemTag() }.asCompound().apply {
                    put("key", key)
                    put("hash", this@ItemBuilder.hashCode())
                    put("type", ItemTagData(this@ItemBuilder.type))
                    put("data", ItemTag().apply {
                        processData.savingKeys.forEach {
                            put(
                                it,
                                ItemTagData.toNBT(processData[it]
                                    .let { variable -> if (variable is Supplier<*>) variable.get() else variable })
                            )
                        }
                    })
                }
            }.also {
                ItemBuildEvent.Post(this, data, it, entity).call()
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST", "SafeCastWithReturn")
        @JvmStatic
        fun deserialize(section: org.bukkit.configuration.ConfigurationSection): ItemBuilder {
            val key = section.name
            val builder = ItemBuilder(key)
            val superItemOption = HashMap<String, Any>()
            val superMetadata = LinkedList<MetaData>()
            section.getStringList("extends")
                .apply {
                    if (isNotEmpty())
                        itemBuilderManager.loading.add(section)
                }.forEach {
                    val superItem = itemBuilderManager[it] ?: return@forEach
                    superItemOption.putAll(superItem.options)
                    superMetadata.addAll(superItem.process)
                }
            section.getList("process")?.forEach { data ->
                data as? Map<String, Any>? ?: return@forEach
                MetaData.deserialize(data).let {
                    builder.process += it
                }
            }
            builder.options.putAll(superItemOption)
            optionManager.initOption(section, builder)
            builder.process.addAll(superMetadata)
            itemBuilderManager.loading.remove(section)
            return builder
        }

        @JvmStatic
        fun serialize(item: ItemStack): Map<String, Any> {
            val data = ItemData.create(item)
            val process = LinkedList<Any>()
            metaManager.sortedMetas.forEach { meta ->
                val metaKey = meta.key
                val any = meta.loadData(data)
                if (any == null || any == meta.default) return@forEach
                val map = LinkedHashMap<String, Any>()
                when (any) {
                    is Map<*, *> -> {
                        if (any.size == 1 && any.containsKey(metaKey)) {
                            process += any
                            return@forEach
                        }
                        map["meta"] = metaKey
                        map.putAll(any as Map<String, Any>)
                    }

                    else -> map[metaKey] = any
                }
                process += map
            }
            return linkedMapOf("process" to process)
        }
    }

    override fun register() {
        itemBuilderManager.register(this)
    }

    override fun serialize(): MutableMap<String, Any> {
        return mutableMapOf("key" to key, "process" to process.map { it.serialize() })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemBuilder) return false

        if (process != other.process) return false

        return true
    }

    override fun hashCode(): Int {
        return process.hashCode()
    }
}
