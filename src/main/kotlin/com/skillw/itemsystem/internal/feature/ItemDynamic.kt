package com.skillw.itemsystem.internal.feature


import com.skillw.asahi.api.AsahiAPI.asahi
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.itemsystem.internal.core.builder.ProcessData
import com.skillw.itemsystem.internal.feature.ItemCache.cacheLore
import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.itemsystem.internal.manager.ISConfig
import com.skillw.pouvoir.util.script.ColorUtil.decolored
import com.skillw.pouvoir.util.toObjList
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.chat.colored
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import java.util.regex.Pattern

object ItemDynamic {
    internal var dynamicPattern: Pattern = Pattern.compile("\\{}")

    @Awake(LifeCycle.ACTIVE)
    fun addFunc() {
        dynamicPattern = Pattern.compile("\\{${ISConfig.unknownDynamic}(?<index>\\d)}")
    }

    private fun String.dynamic(itemStack: ItemStack, entity: LivingEntity): String {
        val matcher = dynamicPattern.matcher(this.decolored())
        if (!matcher.find()) return this
        val buffer = StringBuffer()
        val context = AsahiContext.create().apply {
            put("item", itemStack)
            put("entity", entity)
            if (entity is Player)
                put("player", entity)
        }
        do {
            val index = matcher.group("index")
            val content = itemStack.getContent(index)
            val replaced = content?.asahi(
                context = context,
                namespaces = arrayOf("item_system", "common")
            ).toString()
            matcher.appendReplacement(
                buffer,
                replaced
            )
        } while (matcher.find())
        return matcher.appendTail(buffer).toString().colored()
    }

    internal fun ItemStack.replaceDynamic(entity: LivingEntity) {
        if (!hasItemMeta()) return
        if (!getTag().containsKey("ITEM_SYSTEM")) return
        val meta = itemMeta
        var display = if (meta.hasDisplayName()) meta.displayName else null
        val originLore = cacheLore()
        val newLore = ArrayList<String>()
        display = display?.dynamic(this, entity)
        originLore.forEach { line ->
            newLore.addAll(line.dynamic(this, entity).toObjList())
        }
        meta.setDisplayName(display)
        meta.lore = newLore
        itemMeta = meta
    }

    private const val INDEX_KEY = "!!dynamicIndex!!"

    internal fun ItemStack.getContent(index: String): String? {
        val data = getItemTag().getDeepOrElse("ITEM_SYSTEM.DYNAMIC_DATA", ItemTag()).asCompound()
        if (data.isEmpty()) return null
        return data[index]?.asString()
    }

    internal fun AsahiContext.addDynamic(content: String): String {
        val dynamicData = (this as ProcessData).nbt
            .getOrPut("ITEM_SYSTEM") { ItemTag() }.asCompound()
            .getOrPut("DYNAMIC_DATA") { ItemTag() }.asCompound()
        val index = nextIndex().toString()
        val name = "${ISConfig.unknownDynamic}$index"
        dynamicData[index] = ItemTagData(content)
        return "{$name}"
    }

    private val AsahiContext.dynamicIndexNow
        get() = getOrPut(INDEX_KEY) { 0 } as Int

    private fun AsahiContext.nextIndex(): Int {
        return (dynamicIndexNow + 1).also { put(INDEX_KEY, it) }
    }

}
