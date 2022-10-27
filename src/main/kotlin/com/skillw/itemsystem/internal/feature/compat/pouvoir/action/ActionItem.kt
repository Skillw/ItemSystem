package com.skillw.itemsystem.internal.feature.compat.pouvoir.action


import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.itemsystem.internal.feature.ItemDrop
import com.skillw.itemsystem.internal.feature.ItemDrop.drop
import com.skillw.itemsystem.internal.feature.ItemUpdater.updateItem
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common5.Coerce
import taboolib.library.xseries.parseToXMaterial
import taboolib.module.chat.colored
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getName
import kotlin.math.max

@AutoRegister
object ActionItem : IAction {
    override val actions: Set<String>
        get() = setOf(
            "name",
            "lore",
            "type",
            "cooldown",
            "damage",
            "maxDurability",
            "nbt",
            "rebuild",
            "repair",
            "consume",
            "amount",
            "drop",
            "clone"
        )
    override val type: Class<*>
        get() = ItemStack::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        if (obj !is ItemStack) return null
        with(parser) {
            with(obj) {
                when (action) {
                    "drop" -> {
                        except("at")
                        val loc = parse<Location>()
                        val player = context["player"] as? Player? ?: error("No player found, please define 'player' !")
                        return obj.drop(loc, ItemDrop.DropData(player))
                    }

                    "clone" -> {
                        return clone()
                    }

                    "amount" -> {
                        if (except("to")) {
                            amount = parse()
                            return true
                        }
                        return amount
                    }

                    "name" -> {
                        if (except("to")) {
                            val meta = itemMeta
                            meta.setDisplayName(parseString().colored())
                            itemMeta = meta
                            return true
                        }
                        val player = context["player"] as? Player? ?: error("No player found, please define 'player' !")
                        return getName(player)
                    }

                    "lore" -> {
                        if (except("to")) {
                            val meta = itemMeta
                            meta.lore = parseList().map { it.toString().colored() }
                            itemMeta = meta
                            return true
                        }
                        return itemMeta.lore
                    }

                    "type" -> {
                        if (except("to")) {
                            type = parseString().let {
                                it.parseToXMaterial().parseMaterial() ?: Coerce.toEnum(
                                    it,
                                    Material::class.java,
                                    Material.STONE
                                )
                            }
                            return true
                        }
                        return type
                    }

                    "cooldown" -> {
                        val player = context["player"] as? Player? ?: error("No player found, please define 'player' !")
                        if (except("to")) {
                            player.setCooldown(type, parseInt())
                            return true
                        }
                        return player.getCooldown(this.type)
                    }

                    "damage" -> {
                        if (except("to")) {
                            durability = parse()
                            return true
                        }
                        return durability
                    }

                    "maxDurability" -> {
                        return type.maxDurability
                    }

                    "nbt" -> {
                        if (except("to")) {
                            parse<ItemTag>().saveTo(this)
                            return true
                        }
                        return getTag()
                    }

                    "repair" -> {
                        val amount = parseShort()
                        durability = (max(0, durability - amount)).toShort()
                    }

                    "consume" -> {
                        amount--
                    }

                    "rebuild" -> {
                        val player = context["player"] as? Player? ?: error("No player found, please define 'player' !")
                        var variables = setOf("all")
                        var data: Map<String, Any> = HashMap()
                        if (except("with")) {
                            variables = parseList().map { it.toString() }.toSet()
                            data = parseMap().mapKeys { it.toString() }
                        }
                        updateItem(player, variables, data)
                    }

                    else -> {
                        error("Unknown Item action $action")
                    }
                }
            }
        }
        return null
    }
}