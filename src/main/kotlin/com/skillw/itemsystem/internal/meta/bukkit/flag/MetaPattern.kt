package com.skillw.itemsystem.internal.meta.bukkit.flag

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.annotation.AutoRegister
import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import taboolib.common5.Coerce
import java.util.*

@AutoRegister
object MetaPattern : BaseMeta("flags") {

    override val priority = 8

    override val default = emptyList<String>()

    override fun invoke(memory: Memory) {
        with(memory) {
            builder.patterns.addAll(getList("patterns").mapNotNull {
                val map = it as? Map<*, *>? ?: return@mapNotNull null
                val dye = Coerce.toEnum(map["dye"], DyeColor::class.java, DyeColor.BLACK)
                val pattern = Coerce.toEnum(map["pattern"], PatternType::class.java, PatternType.BASE)
                Pattern(dye, pattern)
            })
        }
    }

    override fun loadData(data: ItemData): Any? {
        val patterns = LinkedList<Any>()
        data.builder.patterns.forEach { pattern ->
            val map = LinkedHashMap<String, Any>()
            map["dye"] = pattern.color.name
            map["pattern"] = pattern.pattern.name
            patterns += map
        }
        return patterns.run { if (isEmpty()) null else this }
    }

}