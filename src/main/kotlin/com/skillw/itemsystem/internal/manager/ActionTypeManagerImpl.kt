package com.skillw.itemsystem.internal.manager

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.action.ActionType
import com.skillw.itemsystem.api.manager.ActionTypeManager
import com.skillw.itemsystem.internal.feature.action.ItemAction.clearCooldwon
import com.skillw.itemsystem.internal.feature.action.ItemAction.run
import com.skillw.pouvoir.api.plugin.SubPouvoir
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ActionTypeManagerImpl : ActionTypeManager() {
    private fun readResolve(): Any = ActionTypeManagerImpl

    override val key: String = "ActionTypeManager"
    override val priority: Int = 2
    override val subPouvoir: SubPouvoir = ItemSystem

    override fun call(
        string: String,
        entity: LivingEntity,
        itemStack: ItemStack,
        receiver: MutableMap<String, Any>.() -> Unit,
    ) {
        return call(this[string] ?: return, entity, itemStack, receiver)
    }

    override fun call(
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
        return item.run(type, context, entity)
    }

    override fun onReload() {
        clearCooldwon()
    }
}
