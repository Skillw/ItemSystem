package com.skillw.itemsystem.internal.core.builder

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.ItemSystem.itemBuilderManager
import com.skillw.itemsystem.ItemSystem.metaManager
import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.event.ItemBuildEvent
import com.skillw.itemsystem.internal.core.meta.data.MetaData
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.common5.mirrorNow
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
    override val process = LinkedList<MetaData>()
    override val lockedNBT = HashSet<String>()
    override var autoUpdate = false

    constructor(key: String, receiver: ItemBuilder.() -> Unit) : this(key) {
        receiver(this)
    }

    override fun build(entity: LivingEntity?, processData: ProcessData): ItemStack {
        val pre = ItemBuildEvent.Post(this, processData, entity)
        pre.call()
        var data = pre.data
        return mirrorNow("product") {
            process.forEach { metaData ->
                metaData.key.invoke(metaData, data)
            }
            val building = ItemBuildEvent.Building(this, data, entity)
            building.call()
            data = building.data
            return@mirrorNow data.result() {
                nbt.getOrPut("ITEM_SYSTEM") { ItemTag() }.asCompound().apply {
                    put("key", key)
                    put("hash", this@ItemBuilder.hashCode())
                    put("data", ItemTag().apply {
                        processData.savingKeys.forEach {
                            put(
                                it,
                                ItemTagData.toNBT(processData[it]
                                    .let { variable -> if (variable is Supplier<*>) variable.get() else variable })
                            )
                        }
                    })
                    putIfAbsent("can-be-placed", ItemTagData("true"))
                    putIfAbsent("can-craft", ItemTagData("false"))
                }
            }.also {
                ItemBuildEvent.After(this, data, it, entity).call()
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST", "SafeCastWithReturn")
        @JvmStatic
        fun deserialize(section: org.bukkit.configuration.ConfigurationSection): ItemBuilder {
            val key = section.name
            val item = ItemBuilder(key)
            val father = LinkedList<MetaData>()
            section.getStringList("extends")
                .apply {
                    if (isNotEmpty())
                        itemBuilderManager.loading.add(section)
                }.forEach {
                    father.addAll(itemBuilderManager[it]?.process ?: return item)
                }
            section.getList("process")?.forEach { data ->
                data as? Map<String, Any>? ?: return@forEach
                MetaData.deserialize(data).let {
                    item.process += it
                }
            }
            item.process.addAll(father)
            item.lockedNBT.addAll(section.getStringList("locked-nbt-keys"))
            item.autoUpdate = section.getBoolean("auto-update", false)
            itemBuilderManager.loading.remove(section)
            return item
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
                        map["meta"] = metaKey
                        map.putAll(any as Map<String, Any>)
                    }

                    else -> map[metaKey] = any
                }
                process += map
            }
            return linkedMapOf("process" to process, "locked-nbt-keys" to emptyList())
        }
    }

    override fun register() {
        ItemSystem.itemBuilderManager.register(this)
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