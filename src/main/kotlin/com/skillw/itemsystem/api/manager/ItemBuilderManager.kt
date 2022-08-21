package com.skillw.itemsystem.api.manager

import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import java.io.File

/**
 * @className MetaManager
 *
 * 用于维护物品构建器
 *
 * @author Glom
 * @date 2022/8/7 7:20 Copyright 2022 user. All rights reserved.
 */
abstract class ItemBuilderManager : Manager, KeyMap<String, BaseItemBuilder>() {
    /** 物品构造器的Yaml文件 */
    abstract val files: Set<File>
}