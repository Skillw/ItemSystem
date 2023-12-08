package com.skillw.itemsystem.internal.manager

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.manager.MetaManager
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.pouvoir.api.plugin.SubPouvoir
import java.util.*

object MetaManagerImpl : MetaManager() {
    private fun readResolve(): Any = MetaManagerImpl

    override val key: String = "MetaManager"
    override val priority: Int = 1
    override val subPouvoir: SubPouvoir = ItemSystem
    override val sortedMetas = LinkedList<BaseMeta>()

    override fun register(value: BaseMeta) {
        sortedMetas.add(value)
        sortedMetas.sort()
        super.register(value)
    }

}
