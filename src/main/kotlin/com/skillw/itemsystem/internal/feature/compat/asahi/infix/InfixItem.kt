package com.skillw.itemsystem.internal.feature.compat.asahi.infix


import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.context.InfixContext
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.itemsystem.api.ItemAPI
import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.itemsystem.internal.feature.ItemDrop
import com.skillw.itemsystem.internal.feature.ItemDrop.drop
import com.skillw.itemsystem.internal.feature.ItemUpdater.updateItem
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

@AsahiInfix
object InfixItem : BaseInfix<ItemStack>(ItemStack::class.java) {
    private fun InfixContext.getPlayer() =
        context["player"] as? Player? ?: error("No player found, please define 'player' !")

    init {
        infix("drop") { obj ->
            expect("at")
            val loc = parse<Location>()
            val player = getPlayer()
            obj.drop(loc, ItemDrop.DropData(player))
        }

        infix("clone") { obj ->
            obj.clone()
        }

        infix("amount") { obj ->
            if (expect("to")) {
                obj.amount = parse()
            }
            obj.amount
        }

        infix("name") { obj ->
            if (expect("to")) {
                val meta = obj.itemMeta
                meta.setDisplayName(parseString().colored())
                obj.itemMeta = meta
                return@infix true
            }
            val player = getPlayer()
            obj.getName(player)
        }

        infix("lore") { obj ->
            if (expect("to")) {
                val meta = obj.itemMeta
                meta.lore = parseList().map { it.toString().colored() }
                obj.itemMeta = meta
                return@infix true
            }
            obj.itemMeta.lore
        }

        infix("material") { obj ->
            if (expect("to")) {
                obj.type = parseString().let {
                    it.parseToXMaterial().parseMaterial() ?: Coerce.toEnum(
                        it,
                        Material::class.java,
                        Material.STONE
                    )
                }
                return@infix true
            }
            obj.type
        }

        infix("cooldown") { obj ->
            val player = getPlayer()
            if (expect("to")) {
                player.setCooldown(obj.type, parseInt())
                return@infix true
            }
            player.getCooldown(obj.type)
        }

        infix("damage") { obj ->
            if (expect("to")) {
                obj.durability = parse()
                return@infix true
            }
            obj.durability
        }

        infix("maxDurability") { obj ->
            obj.type.maxDurability
        }

        infix("nbt") { obj ->
            if (expect("to")) {
                parse<ItemTag>().saveTo(obj)
                return@infix true
            }
            obj.getTag()
        }

        infix("repair") { obj ->
            val amount = parseShort()
            obj.durability = (max(0, obj.durability - amount)).toShort()
            true
        }

        infix("consume") { obj ->
            obj.amount--
        }

        infix("build") { obj ->
            val player = getPlayer()
            val key = parseString()
            val data = HashMap<String, Any>()
            if (expect("with")) {
                data.putAll(parse<HashMap<String, Any>>())
            }
            return@infix ItemAPI.productItem(key, player) {
                it.putAll(data)
            }
        }

        infix("rebuild") { obj ->
            val player = getPlayer()
            var variables = setOf("all")
            var data: Map<String, Any> = HashMap()
            if (expect("with")) {
                variables = parseList().map { it.toString() }.toSet()
                data = parse<HashMap<String, Any>>().mapKeys { it.toString() }
            }
            obj.updateItem(player, variables, data)
        }
    }
}
