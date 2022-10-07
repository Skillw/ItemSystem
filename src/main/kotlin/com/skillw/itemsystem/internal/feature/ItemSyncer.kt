package com.skillw.itemsystem.internal.feature


import com.skillw.itemsystem.internal.feature.ItemCache.cacheLore
import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.pouvoir.api.PouvoirAPI.placeholder
import com.skillw.pouvoir.util.ColorUtils.decolored
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.module.chat.colored
import taboolib.module.nms.ItemTag
import java.util.regex.Pattern

object ItemSyncer {
    internal val syncPattern = Pattern.compile("_sync_(?<nbt>[^_]*)_")


    private fun transform(tag: ItemTag, keys: List<String>): List<Pair<String, String>> {
        return buildList {
            keys.forEach { key ->
                add("_sync_${key}_" to (tag.getDeep(key)?.asString() ?: return@forEach))
            }
        }
    }

    private fun String.syncNBT(
        pair: Pair<String, String>,
        entity: LivingEntity,
    ): String {
        val (key, status) = pair
        return decolored().replace(key, status.placeholder(entity).colored()).colored()
    }

    internal fun ItemStack.syncNBT(entity: LivingEntity) {
        if (!hasItemMeta()) return
        val meta = itemMeta
        var display = if (meta.hasDisplayName()) meta.displayName else null
        val lore = cacheLore()
        val tag = getTag()
        val keys = tag.getDeep("ITEM_SYSTEM.SYNC_KEYS")?.asList()?.map { it.asString() } ?: return
        transform(tag, keys).forEach { pair ->
            display = display?.syncNBT(pair, entity)
            lore.apply { forEachIndexed { index, line -> set(index, line.syncNBT(pair, entity)) } }
        }
        meta.setDisplayName(display)
        meta.lore = lore
        itemMeta = meta
    }

}