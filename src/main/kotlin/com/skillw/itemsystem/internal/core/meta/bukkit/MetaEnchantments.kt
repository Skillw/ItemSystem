package com.skillw.itemsystem.internal.core.meta.bukkit

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.meta.data.Memory.Companion.get
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.enchantments.Enchantment
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce
import taboolib.library.xseries.XEnchantment

@AutoRegister
object MetaEnchantments : BaseMeta("enchantments") {

    override val priority = 10

    override fun invoke(memory: Memory) {
        with(memory) {
            val map = memory.get<Map<String, Any>>("enchantments").analysis()
            builder.enchants.apply {
                map.forEach { (key, value) ->
                    val enchantment = XEnchantment.matchXEnchantment(key)
                        .run { if (isPresent) get().enchant else Enchantment.getByName(key.uppercase()) }
                    if (enchantment == null) {
                        warning("No such enchantment called $key")
                        return@forEach
                    }
                    put(enchantment, Coerce.toInteger(value))
                }
            }
        }
    }


    override fun loadData(data: ItemData): Any? {
        data.itemTag.remove("ench")
        data.itemTag.remove("Enchantments")
        if (data.builder.enchants.isEmpty()) return null
        return mapOf("enchantments" to data.builder.enchants.mapKeys { XEnchantment.matchXEnchantment(it.key).name })
    }

}
