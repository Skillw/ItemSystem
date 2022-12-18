package com.skillw.itemsystem.api.action

import com.skillw.itemsystem.ItemSystem
import com.skillw.pouvoir.api.able.Registrable

class ActionType(override val key: String) : Registrable<String> {

    override fun register() {
        ItemSystem.actionTypeManager.register(this)
    }
}