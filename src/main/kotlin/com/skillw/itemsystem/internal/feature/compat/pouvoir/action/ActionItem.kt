package com.skillw.itemsystem.internal.feature.compat.pouvoir.action


import com.skillw.itemsystem.api.Durability.durability
import com.skillw.itemsystem.api.Durability.maxDurability
import com.skillw.itemsystem.internal.feature.ItemCache.cacheTag
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag

@AutoRegister
object ActionItem : IAction {
    override val actions: Set<String>
        get() = setOf("get", "set", "damage", "repair", "consume")
    override val type: Class<*>
        get() = ItemStack::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        if (obj !is ItemStack) return null
        with(parser) {
            val player = context["player"] as? Player? ?: error("No player found, please define 'player' !")
            with(obj) {
                when (action) {
                    "get" -> {
                        when (parseString()) {
                            "cooldown" -> {
                                return player.getCooldown(this.type)
                            }

                            "durability" -> {
                                return durability()
                            }

                            "maxDurability" -> {
                                return maxDurability()
                            }

                            "nbt" -> {
                                return cacheTag()
                            }

                            else -> {
                                return null
                            }
                        }
                    }

                    "set" -> {
                        when (next()) {
                            "cooldown" -> {
                                val amount = parseInt()
                                player.setCooldown(this.type, amount)
                            }

                            "durability" -> {
                                val amount = parseInt()
                                durability(amount)
                            }

                            "nbt" -> {
                                val tag = parse<ItemTag>()
                                tag.saveTo(this)
                            }

                            else -> {
                                return null
                            }
                        }
                    }

                    "damage" -> {
                        val amount = parseShort()
                        durability(durability() - amount)
                    }

                    "repair" -> {
                        val amount = parseShort()
                        durability(durability() + amount)
                    }

                    "consume" -> {
                        amount--
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