package com.skillw.itemsystem.internal.feature.action

import com.skillw.itemsystem.api.action.ActionType
import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.PouvoirAPI.eval
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.internal.core.function.context.SimpleContext
import com.skillw.pouvoir.util.MapUtils.put
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import java.util.*

internal object ItemAction {
    private val actionCooldown = BaseMap<UUID, BaseMap<Int, LinkedList<String>>>()

    fun clearCooldwon() {
        actionCooldown.clear()
    }

    internal fun ItemStack.run(actionType: ActionType, arguments: Map<String, Any> = emptyMap(), entity: LivingEntity) {
        val tag = getTag()
        val hashCode = hashCode()
        val key = actionType.key

        val argumentsMap = HashMap(arguments)

        argumentsMap["isCooldown"] = actionCooldown.getOrPut(entity.uniqueId) { BaseMap() }
            .getOrPut(hashCode) { LinkedList() }
            .contains(key)

        val actions = tag.getDeep("ITEM_SYSTEM.actions.${key}")?.asList() ?: return
        val cooldown = tag.getDeep("ITEM_SYSTEM.action_cooldown.${key}")?.asLong() ?: 0L
        submitAsync {
            actions.forEach {
                it.asString().run {
                    if (startsWith("js_eval::"))
                        scriptManager.evalJs<Any?>(substring(10), arguments = argumentsMap)
                    else if (startsWith("js_invoke::"))
                        scriptManager.invoke<Any?>(substring(11), arguments = argumentsMap)
                    else eval(
                        arrayOf("common"),
                        context = SimpleContext().apply { putAll(argumentsMap); })
                }
            }
        }
        if (cooldown <= 0) return
        actionCooldown.getOrPut(entity.uniqueId) { BaseMap() }.put(hashCode, key)
        submit(delay = cooldown) {
            actionCooldown.getOrPut(entity.uniqueId) { BaseMap() }.getOrPut(hashCode) { LinkedList() }
                .remove(key)
        }

    }
}