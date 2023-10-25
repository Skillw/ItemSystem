package com.skillw.itemsystem.internal.feature.block

import com.skillw.itemsystem.api.ItemAPI
import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.itemsystem.util.GsonUtils.parseToMap
import com.skillw.itemsystem.util.NBTUtils.obj
import com.skillw.pouvoir.util.encodeJson
import com.skillw.pouvoir.util.loadYaml
import com.skillw.pouvoir.util.toMap
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.io.newFile
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.submitAsync
import java.io.File


object BlockData {
    val blockData = HashMap<Location, Map<String, Any>>()

    fun push(location: Location, item: ItemStack) {
        val nbt = item.getTag()
        val key = nbt.getDeep("ITEM_SYSTEM.key")!!.asString()
        val data = nbt.getDeep("ITEM_SYSTEM.data")!!.obj()
        blockData[location] = mapOf("item_key" to key, "data" to data)
    }

    fun pull(location: Location, player: Player): ItemStack? {
        return blockData.remove(location)?.run {
            val key = this["item_key"].toString()
            val data = this["data"] as Map<String, Any>
            ItemAPI.productItem(key, player) {
                it.putAll(data)
            }
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun enable() {
        val file = newFile(File(getDataFolder(), "data/data.yml"))
        val yaml = file.loadYaml() ?: return
        yaml.toMap().forEach { (key, value) ->
            value as Map<String, Any>
            val itemKey = value["item_key"].toString()
            val data = value["data"].toString().parseToMap()
            val location = with(key.split(",")) {
                val world = Bukkit.getWorld(this[0]) ?: return@forEach
                val x = this[1].toDouble()
                val y = this[2].toDouble()
                val z = this[3].toDouble()
                Location(world, x, y, z)
            }
            blockData[location] = mapOf("item_key" to itemKey, "data" to data)
        }
        saveTask()
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        save()
    }

    private fun save() {
        var file = File(getDataFolder(), "data/data.yml")
        file.delete()
        file = newFile(File(getDataFolder(), "data/data.yml"))
        val yaml = file.loadYaml() ?: return
        blockData.forEach { (loc, map) ->
            val location = with(loc) { "${world.name},$blockX,$blockY,$blockZ" }
            val key = map["item_key"].toString()
            val data = (map["data"] as Map<String, Any>).encodeJson()
            yaml[location] = linkedMapOf("item_key" to key, "data" to data)
        }
        yaml.save(file)
    }


    fun saveTask() {
        submitAsync(period = 12000) {
            save()
        }
    }
}
