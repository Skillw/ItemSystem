package com.skillw.itemsystem.internal.feature.compat.pouvoir.action


import com.skillw.itemsystem.api.ItemAPI
import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.itemsystem.internal.feature.ItemDrop
import com.skillw.itemsystem.internal.feature.ItemDrop.drop
import com.skillw.itemsystem.internal.feature.ItemUpdater.updateItem
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction
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
object ActionItem : PouAction<ItemStack>(ItemStack::class.java) {
    private fun Parser.getPlayer() = context["player"] as? Player? ?: error("No player found, please define 'player' !")

    init {
        addExec("drop") { obj ->
            except("at")
            val loc = parse<Location>()
            val player = getPlayer()
            obj.drop(loc, ItemDrop.DropData(player))
        }

        addExec("clone") { obj ->
            obj.clone()
        }

        addExec("amount") { obj ->
            if (except("to")) {
                obj.amount = parse()
            }
            obj.amount
        }

        addExec("name") { obj ->
            if (except("to")) {
                val meta = obj.itemMeta
                meta.setDisplayName(parseString().colored())
                obj.itemMeta = meta
                return@addExec true
            }
            val player = getPlayer()
            obj.getName(player)
        }

        addExec("lore") { obj ->
            if (except("to")) {
                val meta = obj.itemMeta
                meta.lore = parseList().map { it.toString().colored() }
                obj.itemMeta = meta
                return@addExec true
            }
            obj.itemMeta.lore
        }

        addExec("type") { obj ->
            if (except("to")) {
                obj.type = parseString().let {
                    it.parseToXMaterial().parseMaterial() ?: Coerce.toEnum(
                        it,
                        Material::class.java,
                        Material.STONE
                    )
                }
                return@addExec true
            }
            obj.type
        }

        addExec("cooldown") { obj ->
            val player = getPlayer()
            if (except("to")) {
                player.setCooldown(obj.type, parseInt())
                true
            }
            player.getCooldown(obj.type)
        }

        addExec("damage") { obj ->
            if (except("to")) {
                obj.durability = parse()
                return@addExec true
            }
            obj.durability
        }

        addExec("maxDurability") { obj ->
            obj.type.maxDurability
        }

        addExec("nbt") { obj ->
            if (except("to")) {
                parse<ItemTag>().saveTo(obj)
                return@addExec true
            }
            obj.getTag()
        }

        addExec("repair") { obj ->
            val amount = parseShort()
            obj.durability = (max(0, obj.durability - amount)).toShort()
            true
        }

        addExec("consume") { obj ->
            obj.amount--
        }

        addExec("build") { obj ->
            val player = getPlayer()
            val key = parseString()
            val data = HashMap<String, Any>()
            if (except("with")) {
                data.putAll(parseMap())
            }
            return@addExec ItemAPI.productItem(key, player) {
                it.putAll(data)
            }
        }

        addExec("rebuild") { obj ->
            val player = getPlayer()
            var variables = setOf("all")
            var data: Map<String, Any> = HashMap()
            if (except("with")) {
                variables = parseList().map { it.toString() }.toSet()
                data = parseMap().mapKeys { it.toString() }
            }
            obj.updateItem(player, variables, data)
        }
    }
}