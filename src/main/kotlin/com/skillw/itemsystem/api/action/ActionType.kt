package com.skillw.itemsystem.api.action

import com.skillw.itemsystem.ItemSystem
import com.skillw.pouvoir.api.plugin.map.component.Registrable

class ActionType(override val key: String) : Registrable<String> {

    override fun register() {
        ItemSystem.actionTypeManager.register(this)
    }
}
