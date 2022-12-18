package com.skillw.itemsystem.internal.command.sub

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.internal.command.ISCommand.soundClick
import com.skillw.itemsystem.internal.command.ISCommand.soundFail
import com.skillw.itemsystem.internal.command.ISCommand.soundSuccess
import com.skillw.itemsystem.internal.core.option.OptionAbstract.abstract
import com.skillw.itemsystem.internal.feature.ItemDrop
import com.skillw.itemsystem.internal.feature.ItemDrop.drop
import com.skillw.itemsystem.internal.feature.ItemDrop.effectDrop
import com.skillw.itemsystem.internal.feature.effect.RandomItemEffect
import com.skillw.itemsystem.internal.feature.product.ProductData
import com.skillw.itemsystem.util.ItemUtils.displayClone
import com.skillw.itemsystem.util.ItemUtils.displayItem
import com.skillw.pouvoir.util.PlayerUtils.soundFinish
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getProxyPlayer
import taboolib.common.platform.function.onlinePlayers
import taboolib.common.platform.function.submitAsync
import taboolib.common.util.sync
import taboolib.common5.Coerce
import taboolib.module.chat.TellrawJson
import taboolib.module.lang.asLangText
import taboolib.module.lang.sendLang
import taboolib.module.nms.getName
import taboolib.platform.util.giveItem
import taboolib.platform.util.toProxyLocation

object ItemProductCommand {
    private fun product(
        argument: String,
        productData: String,
        sender: ProxyCommandSender,
        proxyPlayer: ProxyPlayer? = null,
    ): ProductData {
        val itemKey = if (!argument.contains(" ")) {
            argument
        } else {
            argument.substringBefore(" ")
        }
        val item = ItemSystem.itemBuilderManager[itemKey]
        if (item == null) {
            sender.sendLang("command-valid-item", itemKey)
            return ProductData(ArrayList(), false)
        }
        if (item.abstract) {
            sender.sendLang("item-cant-be-produced", itemKey)
            return ProductData(ArrayList(), false)
        }
        return ProductData.product(item, productData, proxyPlayer?.cast<Player>())
    }

    @Suppress("DEPRECATION")
    private fun give(
        argument: String,
        sender: ProxyCommandSender,
        proxyPlayer: ProxyPlayer? = null,
    ) {
        submitAsync {
            val itemKey = argument.run { if (contains(" ")) substringBefore(" ") else this }
            val productData = argument.run { if (contains(" ")) substringAfter(" ") else this }
            val data = product(itemKey, productData, sender, proxyPlayer)
            val itemStacks = data.items
            val isSame = data.isSame
            val player = proxyPlayer?.cast<Player>() ?: sender.cast()
            if (itemStacks.isEmpty()) return@submitAsync
            if (!isSame) {
                sync {
                    player.soundFinish()
                    for (itemStack in itemStacks) {
                        player.giveItem(itemStack)
                        sender.sendGiveMessage(player, itemStack, 1)
                    }
                }
                return@submitAsync
            }
            sync {
                player.giveItem(itemStacks)
            }
            player.soundFinish()
            sender.sendGiveMessage(player, itemStacks[0], itemStacks.size)
        }
    }

    @Suppress("DEPRECATION")
    private fun ProxyCommandSender.sendGiveMessage(player: Player, itemStack: ItemStack, amount: Int) {
        val item = itemStack.displayClone(player)
        TellrawJson()
            .append(asLangText("command-give-item", player.displayName, amount, item.getName()))
            .append(
                TellrawJson()
                    .append(console().asLangText("show-item"))
                    .displayItem(item)
            )
            .sendTo(this)
    }


    val get = subCommand {
        dynamic {
            suggestion<ProxyPlayer>(uncheck = true) { _, _ ->
                ItemSystem.itemBuilderManager.map { it.key }
            }
            execute<ProxyPlayer> { sender, _, argument ->
                if (!argument.contains(" ")) {
                    give(argument, sender, sender)
                } else {
                    give(argument, sender, sender)
                }
            }
        }
    }

    val give = subCommand {
        dynamic {
            suggestion<ProxyCommandSender>(uncheck = true) { sender, _ ->
                sender.soundClick()
                onlinePlayers().map { it.name }
            }
            dynamic {
                suggestion<ProxyCommandSender>(uncheck = true) { _, _ ->
                    ItemSystem.itemBuilderManager.map { it.key }
                }
                execute<ProxyCommandSender> { sender, context, argument ->
                    val playerName = context.argument(-1)
                    val proxyPlayer = getProxyPlayer(playerName)
                    if (proxyPlayer == null) {
                        sender.sendLang("command-valid-player", playerName)
                        sender.soundFail()
                        return@execute
                    }
                    give(argument, sender, proxyPlayer)
                }
            }
        }
    }
    val drop = subCommand {
        dynamic {
            suggestion<ProxyCommandSender>(uncheck = true) { sender, _ ->
                sender.soundClick()
                onlinePlayers().map { it.name }
            }
            dynamic {
                suggestion<ProxyCommandSender>(uncheck = true) { sender, _ ->
                    sender.soundClick()
                    ItemSystem.itemBuilderManager.map { it.key }
                }
                dynamic {
                    suggestion<ProxyCommandSender>(uncheck = true) { sender, _ ->
                        sender.soundClick()
                        Bukkit.getWorlds().map { it.name }
                    }
                    dynamic {
                        suggestion<ProxyPlayer>(uncheck = true) { sender, _ ->
                            sender.soundClick()
                            listOf(sender.location.x.toString())
                        }
                        restrict<ProxyCommandSender> { _, _, argument ->
                            Coerce.asDouble(argument).isPresent
                        }
                        dynamic {
                            suggestion<ProxyPlayer>(uncheck = true) { sender, _ ->
                                sender.soundClick()
                                listOf(sender.location.y.toString())
                            }
                            restrict<ProxyCommandSender> { _, _, argument ->
                                Coerce.asDouble(argument).isPresent
                            }
                            dynamic {
                                suggestion<ProxyPlayer>(uncheck = true) { sender, _ ->
                                    sender.soundClick()
                                    listOf(sender.location.z.toString())
                                }
                                restrict<ProxyCommandSender> { _, _, argument ->
                                    Coerce.asDouble(argument).isPresent
                                }
                                execute<ProxyCommandSender> { sender, context, argument ->
                                    val playerName = context.argument(-5)
                                    val proxyPlayer = getProxyPlayer(playerName)
                                    if (proxyPlayer == null) {
                                        sender.sendLang("command-valid-player", playerName)
                                        sender.soundFail()
                                        return@execute
                                    }

                                    val itemKey = context.argument(-4)

                                    val worldKey = context.argument(-3)
                                    val world = Bukkit.getWorld(worldKey)
                                    if (world == null) {
                                        sender.sendLang("command-valid-world", worldKey)
                                        sender.soundFail()
                                        return@execute
                                    }

                                    val x = context.argument(-2).toDouble()
                                    val y = context.argument(-1).toDouble()
                                    var z: Double
                                    submitAsync {
                                        val productData: ProductData
                                        if (!argument.contains(" ")) {
                                            z = argument.toDouble()
                                            productData = product(itemKey, " ", sender, proxyPlayer)
                                        } else {
                                            z = argument.substringBefore(" ").toDouble()
                                            productData = product(
                                                itemKey,
                                                argument.substringAfter(" "),
                                                sender,
                                                proxyPlayer
                                            )
                                        }
                                        val location = Location(world, x, y, z)
                                        val itemStacks = productData.items
                                        val isSame = productData.isSame
                                        if (itemStacks.isEmpty()) return@submitAsync

                                        val dropData = ItemDrop.DropData(proxyPlayer.cast())
                                        if (productData.demand.tags.contains("effect")) {
                                            sync {
                                                itemStacks.effectDrop(
                                                    RandomItemEffect(location.toProxyLocation()),
                                                    Vector.getRandom(),
                                                    dropData
                                                )
                                            }
                                        } else {
                                            sync { itemStacks.forEach { it.drop(location, dropData) } }
                                        }

                                        proxyPlayer.soundSuccess()
                                        itemStacks.forEach {
                                            if (!isSame)
                                                sender.sendDropMessage(
                                                    proxyPlayer.cast(),
                                                    it,
                                                    1,
                                                    location
                                                )
                                        }
                                        if (isSame)
                                            sender.sendDropMessage(
                                                proxyPlayer.cast(),
                                                itemStacks[0],
                                                itemStacks.size,
                                                location
                                            )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun ProxyCommandSender.sendDropMessage(
        player: Player,
        itemStack: ItemStack,
        amount: Int,
        location: Location,
    ) {
        val item = itemStack.displayClone(player)
        with(location) {
            TellrawJson()
                .append(
                    TellrawJson()
                        .append(asLangText("command-drop-item-item", item.getName(), amount))
                )
                .append(
                    TellrawJson()
                        .append(console().asLangText("show-item"))
                        .displayItem(item)
                )
                .append(
                    TellrawJson()
                        .append(
                            asLangText("command-drop-item-loc", world.name, x, y, z)
                        )
                        .hoverText(asLangText("command-drop-item-loc-hover"))
                        .runCommand("/tp $x $y $z")
                )
                .sendTo(this@sendDropMessage)
        }
    }

}