package com.skillw.itemsystem.api.manager

import com.skillw.itemsystem.internal.core.meta.data.MetaData
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.BaseMap

/**
 * @className GlobalManager
 *
 * 用于维护所有的全局元数据
 *
 * @author Glom
 * @date 2022/8/7 7:20 Copyright 2022 user. All rights reserved.
 */
abstract class GlobalManager : Manager, BaseMap<String, MetaData>()
