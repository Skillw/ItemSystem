package com.skillw.itemsystem.api.manager

import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.vartype.VariableType
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import java.util.function.Supplier

/**
 * @className VarTypeManager
 *
 * 用于维护所有的 变量(节点)类型
 *
 * @author Glom
 * @date 2022年8月16日 21:02:44 Copyright 2022 user. All rights reserved.
 */
abstract class VarTypeManager : Manager, KeyMap<String, VariableType>() {
    /**
     * 在元记忆的Context中创建变量，返回一个Supplier
     *
     * 若cache为true，则返回一个Supplier，在第一次调用时确定变量值
     * 若cache为false，则返回一个Supplier，该变量一直不会确定变量值
     *
     * @param memory
     * @return
     */
    abstract fun createVar(memory: Memory): Supplier<Any>?
}