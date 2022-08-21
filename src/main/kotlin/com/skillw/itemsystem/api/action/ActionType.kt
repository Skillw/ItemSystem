package com.skillw.itemsystem.api.action

import com.skillw.itemsystem.internal.feature.action.ItemAction.run
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.map.LowerKeyMap
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ActionType(override val key: String) : Registrable<String> {

    companion object {
        @JvmStatic
        internal val actionTypes = LowerKeyMap<ActionType>()

        @JvmStatic
        fun call(
            string: String,
            player: Player,
            itemStack: ItemStack,
            receiver: MutableMap<String, Any>.() -> Unit,
        ) {
            call(actionTypes[string] ?: return, player, itemStack, receiver)
        }

        private fun call(
            type: ActionType,
            player: Player,
            item: ItemStack,
            receiver: MutableMap<String, Any>.() -> Unit,
        ) {
            val context = HashMap<String, Any>()
            receiver(context)
            context["player"] = player
            context["item"] = item
            item.run(type, context)
        }
    }


    override fun register() {
        actionTypes.register(this)
    }
}