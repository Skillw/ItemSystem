package com.skillw.itemsystem.util.nms


import com.skillw.itemsystem.api.glow.GlowColor
import net.minecraft.server.v1_16_R3.*
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.library.reflex.Reflex.Companion.unsafeInstance
import taboolib.module.nms.MinecraftVersion
import java.util.*

/**
 * @className NMS
 *
 * @author Glom
 * @date 2022/8/9 22:24 Copyright 2022 user. All rights reserved.
 */
class NMSImpl : NMS() {


    override fun computeCraftItems(player: Player, packet: Any, func: (org.bukkit.inventory.ItemStack) -> Unit) {
        packet.getProperty<List<ItemStack>>(itemsField)?.onEach {
            compute(it, func)
        }
    }

    private fun compute(item: ItemStack, func: (org.bukkit.inventory.ItemStack) -> Unit) {
        val bukkit = CraftItemStack.asBukkitCopy(item)
        func(bukkit)
        CraftItemStack.setItemMeta(item, bukkit.itemMeta)
    }


    override fun computeCraftItem(player: Player, packet: Any, func: (org.bukkit.inventory.ItemStack) -> Unit) {
        val item = packet.getProperty<ItemStack>(itemField) ?: return
        compute(item, func)
    }

    private fun ChatColor.toNMS(): EnumChatFormat {
        return try {
            EnumChatFormat.valueOf(name)
        } catch (e: IllegalArgumentException) {
            EnumChatFormat.RESET
        }
    }

    private fun <T> T.withFields(vararg pairs: Pair<String, Any>?): T {
        pairs.filterNotNull().forEach { (key, value) ->
            this?.setProperty(key, value)
        }
        return this
    }

    private fun Any.setColor(chatColor: ChatColor): Any {
        val color = chatColor.toNMS()
        when (versionType) {
            VersionType.UNIVERSAL -> {
                val parameters = getProperty<Optional<Any>>("k")?.get() ?: return this
                parameters.setProperty("f", color)
                setProperty("k", Optional.of(parameters))
            }

            VersionType.AQUATIC -> {
                setProperty("g", color)
            }

            VersionType.LEGACY -> {
                setProperty("c", color.toString())
            }
        }
        return this
    }

    private fun Any.init(name: String, type: Int): Any {
        when (versionType) {
            VersionType.LEGACY -> {
                withFields(
                    "a" to name,
                    "b" to "",
                    "c" to "",
                    "d" to "",
                    "e" to "",
                    "f" to "",
                    "g" to 0,
                    "h" to ArrayList<String>(),
                    "i" to type,
                    "j" to 3
                )
            }

            VersionType.AQUATIC -> {
                withFields(
                    "a" to name,
                    "b" to ChatComponentText.d,
                    "c" to ChatComponentText.d,
                    "d" to ChatComponentText.d,
                    "e" to ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS.e,
                    "f" to ScoreboardTeamBase.EnumTeamPush.ALWAYS.e,
                    "g" to EnumChatFormat.WHITE,
                    "h" to ArrayList<String>(),
                    "i" to type,
                    "j" to 3
                )
            }

            VersionType.UNIVERSAL -> {
                withFields(
                    "h" to type,
                    "i" to name,
                    "j" to ArrayList<String>(),
                    "k" to Optional.of(
                        Class.forName("net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam\$b")
                            .unsafeInstance()
                    )
                )
            }
        }
        return this
    }

    override fun buildColorPacket(chatColor: ChatColor, type: GlowColor.Operation, entity: Entity?): Any {
        val name = "IS_${chatColor.name}"
        val packet =
            PacketPlayOutScoreboardTeam::class.java.unsafeInstance()
                .init(name, type.id)
                .setColor(chatColor)
        when (type) {
            GlowColor.Operation.CREATE -> {
                return when (versionType) {
                    VersionType.UNIVERSAL -> {
                        val params =
                            packet.getProperty<Optional<Any>>(
                                "k"
                            )?.get() ?: return packet
                        if (MinecraftVersion.major >= 11)
                            params.withFields(
                                "a" to net.minecraft.network.chat.IChatBaseComponent.b(name),
                                "b" to net.minecraft.network.chat.IChatBaseComponent.b(chatColor.toString()),
                                "c" to net.minecraft.network.chat.IChatBaseComponent.b(""),
                                "d" to "always",
                                "e" to "always",
                                "f" to chatColor.toNMS(),
                                "g" to 3,
                            )
                        else
                            params.withFields(
                                "a" to ChatComponentText(name),
                                "b" to ChatComponentText(chatColor.toString()),
                                "c" to ChatComponentText(""),
                                "d" to "always",
                                "e" to "always",
                                "f" to chatColor.toNMS(),
                                "g" to 3,
                            )
                        packet.setProperty("k", Optional.of(params))
                        return packet
                    }

                    else -> packet
                }
            }

            GlowColor.Operation.ADD_ENTITY,
            GlowColor.Operation.REMOVE_ENTITY,
            -> return packet.apply {
                getProperty<MutableList<String>>(entitiesField)?.add(entity?.uniqueId.toString())
            }
        }
    }

    override fun getEntity(world: World, id: Int): Entity? {
        return (world as CraftWorld).handle.getEntity(id)?.bukkitEntity
    }

}