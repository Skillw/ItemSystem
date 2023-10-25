package com.skillw.itemsystem.api.manager

import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.KeyMap

/**
 * @className MetaManager
 *
 * 用于维护所有的 Meta (元)
 *
 * @author Glom
 * @date 2022/8/7 7:20 Copyright 2022 user. All rights reserved.
 */
abstract class MetaManager : Manager, KeyMap<String, BaseMeta>() {
    abstract val sortedMetas: List<BaseMeta>
}
