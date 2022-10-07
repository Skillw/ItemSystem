package com.skillw.itemsystem.internal.feature.action

import com.skillw.itemsystem.api.action.ActionType
import com.skillw.itemsystem.internal.feature.ItemCache.getTag
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.PouvoirAPI.eval
import com.skillw.pouvoir.internal.core.function.context.SimpleContext
import org.bukkit.inventory.ItemStack

internal object ItemAction {
    internal fun ItemStack.run(actionType: ActionType, arguments: Map<String, Any> = emptyMap()) {
        val tag = getTag()
        val actions = tag.getDeep("ITEM_SYSTEM.actions.${actionType.key}")?.asList() ?: return
        actions.forEach {
            it.asString().run {
                if (startsWith("js_eval::"))
                    scriptManager.evalJs<Any?>(substring(10), arguments = arguments)
                else if (startsWith("js_invoke::"))
                    scriptManager.invoke<Any?>(substring(11), arguments = arguments)
                else eval(
                    arrayOf("common"),
                    context = SimpleContext().apply { putAll(arguments); })
            }
        }
    }
}