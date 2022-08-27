package com.skillw.itemsystem.api.action

import com.skillw.itemsystem.internal.feature.action.ItemAction.run
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.map.LowerKeyMap
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ActionType(override val key: String) : Registrable<String> {

    companion object {
        @JvmStatic
        internal val actionTypes = LowerKeyMap<ActionType>()

        @JvmStatic
        fun call(
            string: String,
            entity: LivingEntity,
            itemStack: ItemStack,
            receiver: MutableMap<String, Any>.() -> Unit,
        ) {
            call(actionTypes[string] ?: return, entity, itemStack, receiver)
        }

        private fun call(
            type: ActionType,
            entity: LivingEntity,
            item: ItemStack,
            receiver: MutableMap<String, Any>.() -> Unit,
        ) {
            val context = HashMap<String, Any>()
            receiver(context)
            context["entity"] = entity
            if (entity is Player) context["player"] = entity
            context["item"] = item
            item.run(type, context)
        }
    }


    override fun register() {
        actionTypes.register(this)
    }
}