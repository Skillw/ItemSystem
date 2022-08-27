package com.skillw.itemsystem.internal.feature.compat.buffsystem

import com.skillw.attsystem.api.event.EquipmentUpdateEvent
import com.skillw.buffsystem.BuffSystem
import com.skillw.buffsystem.BuffSystem.buffManager
import com.skillw.buffsystem.api.BuffAPI.hasBuff
import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.meta.data.Memory.Companion.get
import com.skillw.itemsystem.util.NBTUtils.toMutableMap
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.util.MapUtils.put
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.event.OptionalEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

@AutoRegister(test = "com.skillw.buffsystem.api.effect.BaseEffect")
object BuffsMeta : BaseMeta("buffs") {
    private const val BUFF_KEY = "BUFF_DATA"
    override fun invoke(memory: Memory) {
        with(memory) {
            val buffs = get<Map<String, Any>>("buffs")
            buffs.forEach { (key, data) ->
                if (!buffManager.containsKey(key)) return@forEach
                nbt.getOrPut(BUFF_KEY) { ItemTag() }.asCompound()[key] = ItemTag.toNBT(data)
            }
        }
    }


    override fun loadData(data: ItemData): Any? {
        return data.itemTag.remove(BUFF_KEY)?.asCompound()?.toMutableMap()
    }


    private val tags = ConcurrentHashMap<Int, ItemTag>()
    private val data = BaseMap<UUID, HashSet<String>>()

    @SubscribeEvent(bind = "com.skillw.attsystem.api.event.EquipmentUpdateEvent")
    fun e(optional: OptionalEvent) {
        val event = optional.get<EquipmentUpdateEvent>()
        val entity = event.entity
        if (entity !is LivingEntity) return
        val sets = HashSet<String>()
        event.equipmentData.values.forEach inner@{
            it.values.forEach { item ->
                if (item.isAir()) return@forEach
                tags.getOrPut(item.hashCode()) { item.getItemTag() }[BUFF_KEY]?.asCompound()
                    ?.forEach buffKey@{ (key, data) ->
                        val buff = buffManager[key] ?: return@buffKey
                        val dataKey = entity.uniqueId.toString() + "-item-$key"
                        sets.add(dataKey)
                        if (entity.hasBuff(dataKey)) return@buffKey
                        BuffsMeta.data.put(entity.uniqueId, dataKey)
                        BuffSystem.buffDataManager.giveBuff(entity, dataKey, buff) { buffData ->
                            buffData.putAll(data.asCompound().toMutableMap())
                            buffData["duration"] = -1
                        }
                    }
            }
        }
        data[entity.uniqueId]?.forEach {
            if (!sets.contains(it)) {
                BuffSystem.buffDataManager.removeBuff(entity, it)
                data.get(entity.uniqueId)!!.remove(it)
            }
        }
    }


}