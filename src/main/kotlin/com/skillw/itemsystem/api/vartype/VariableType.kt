package com.skillw.itemsystem.api.vartype

import com.skillw.itemsystem.ItemSystem
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.pouvoir.api.able.Registrable

/**
 * @className VariableType
 *
 * @author Glom
 * @date 2022/8/16 20:51 Copyright 2022 user. All rights reserved.
 */
abstract class VariableType(override val key: String, vararg val alias: String) : Registrable<String> {
    /**
     * 声明变量
     *
     * 如果变量元的cache=true,才会缓存结果，反之每次调用都会执行一遍
     *
     * @param memory 元记忆
     * @return 变量
     */
    abstract fun createVar(memory: Memory): Any

    /** 注册至变量类型管理器 */
    override fun register() {
        ItemSystem.varTypeManager.register(this)
    }
}