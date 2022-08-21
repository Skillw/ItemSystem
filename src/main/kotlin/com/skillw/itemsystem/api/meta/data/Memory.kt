package com.skillw.itemsystem.api.meta.data

import com.skillw.itemsystem.api.builder.IProcessData
import com.skillw.itemsystem.internal.builder.ProcessData
import com.skillw.itemsystem.internal.meta.data.MetaData
import taboolib.common5.Coerce

/**
 * @className Memory
 *
 * 元记忆 包含过程数据与元数据
 *
 * @author Glom
 * @date 2022/8/14 7:45 Copyright 2022 user. All rights reserved.
 */
data class Memory(val metaData: MetaData, val processData: ProcessData) : IMetaData by metaData,
    IProcessData by processData {

    /**
     * 根据键获取任意对象
     *
     * @param key 键
     * @return 对象
     */
    fun getAny(key: String): Any {
        return get(key)
    }

    /**
     * 根据键获取 列表
     *
     * @param key 键
     * @return 列表
     */
    fun getList(key: String): List<Any> {
        return get(key)
    }

    /**
     * 根据键获取 short
     *
     * @param key 键
     * @return short
     */
    fun getShort(key: String): Short {
        return get(key)
    }

    /**
     * 根据键获取 byte
     *
     * @param key 键
     * @return byte
     */
    fun getByte(key: String): Byte {
        return get(key)
    }

    /**
     * 根据键获取 boolean
     *
     * @param key 键
     * @return boolean
     */
    fun getBoolean(key: String): Boolean {
        return get(key)
    }

    /**
     * 根据键获取 double
     *
     * @param key 键
     * @return double
     */
    fun getDouble(key: String): Double {
        return get(key)
    }

    /**
     * 根据键获取 float
     *
     * @param key 键
     * @return float
     */
    fun getFloat(key: String): Float {
        return get(key)
    }

    /**
     * 根据键获取 long
     *
     * @param key 键
     * @return long
     */
    fun getLong(key: String): Long {
        return get(key)
    }

    /**
     * 根据键获取 int
     *
     * @param key 键
     * @return int
     */
    fun getInt(key: String): Int {
        return get(key)
    }

    /**
     * 根据键获取字符串
     *
     * @param key 键
     * @return 字符串
     */
    fun getString(key: String): String {
        return get(key)
    }

    /**
     * 根据键获取任意对象
     *
     * @param key 键
     * @param default 如果没有找到则返回的默认值
     * @return 对象
     */
    fun getAny(key: String, default: Any? = null): Any {
        return get(key, default)
    }

    /**
     * 根据键获取 列表
     *
     * @param key 键
     * @param default 如果没有找到则返回的默认值
     * @return 列表
     */
    fun getList(key: String, default: List<Any>? = null): List<Any> {
        return get(key, default)
    }

    /**
     * 根据键获取 short
     *
     * @param key 键
     * @param default 如果没有找到则返回的默认值
     * @return short
     */
    fun getShort(key: String, default: Short? = null): Short {
        return get(key, default)
    }

    /**
     * 根据键获取 byte
     *
     * @param key 键
     * @param default 如果没有找到则返回的默认值
     * @return byte
     */
    fun getByte(key: String, default: Byte? = null): Byte {
        return get(key, default)
    }

    /**
     * 根据键获取 boolean
     *
     * @param key 键
     * @param default 如果没有找到则返回的默认值
     * @return boolean
     */
    fun getBoolean(key: String, default: Boolean? = null): Boolean {
        return get(key, default)
    }

    /**
     * 根据键获取 double
     *
     * @param key 键
     * @param default 如果没有找到则返回的默认值
     * @return double
     */
    fun getDouble(key: String, default: Double? = null): Double {
        return get(key, default)
    }

    /**
     * 根据键获取 float
     *
     * @param key 键
     * @param default 如果没有找到则返回的默认值
     * @return float
     */
    fun getFloat(key: String, default: Float? = null): Float {
        return get(key, default)
    }

    /**
     * 根据键获取 long
     *
     * @param key 键
     * @param default 如果没有找到则返回的默认值
     * @return long
     */
    fun getLong(key: String, default: Long? = null): Long {
        return get(key, default)
    }

    /**
     * 根据键获取 int
     *
     * @param key 键
     * @param default 如果没有找到则返回的默认值
     * @return int
     */
    fun getInt(key: String, default: Int? = null): Int {
        return get(key, default)
    }

    /**
     * 根据键获取字符串
     *
     * @param key 键
     * @param default 如果没有找到则返回的默认值
     * @return 字符串
     */
    fun getString(key: String, default: String? = null): String {
        return get(key, default)
    }

    companion object {
        /**
         * 获取任意对象
         *
         * @param key String 键
         * @param default T? 默认值
         * @return T 值类型
         * @receiver Memory 元记忆
         */
        @JvmStatic
        internal inline fun <reified T> Memory.get(key: String, default: T? = null): T {
            return when (T::class) {
                String::class -> getString(key, processData) as? T?
                Int::class -> getInt(key, processData) as? T?
                Long::class -> getLong(key, processData) as? T?
                Float::class -> getFloat(key, processData) as? T?
                Double::class -> getDouble(key, processData) as? T?
                Boolean::class -> getBoolean(key, processData) as? T?
                Byte::class -> getByte(key, processData) as? T?
                Short::class -> getShort(key, processData) as? T?
                List::class -> getList(key, processData) as? T?
                else -> getAny(key, processData) as? T?
            } ?: default ?: error(
                "get ${T::class.simpleName} error"
            )
        }
    }

    /**
     * 根据键获取any
     *
     * @param key
     * @param processData
     * @return
     */
    internal fun getAny(key: String, processData: ProcessData): Any? {
        return (metaData.get(key) ?: processData[key])
    }

    /**
     * 根据键获取list
     *
     * @param key
     * @param processData
     * @return
     */
    internal fun getList(key: String, processData: ProcessData): List<Any>? {
        return ((metaData.get(key) ?: processData[key]) as? List<Any>?)?.map { it.analysis() }
    }

    /**
     * 根据键获取short
     *
     * @param key
     * @param processData
     * @return
     */
    internal fun getShort(key: String, processData: ProcessData): Short? {
        return Coerce.toShort((metaData.get(key) ?: processData[key] ?: return null).analysis())
    }

    /**
     * 根据键获取byte
     *
     * @param key
     * @param processData
     * @return
     */
    internal fun getByte(key: String, processData: ProcessData): Byte? {
        return Coerce.toByte((metaData.get(key) ?: processData[key] ?: return null).analysis())
    }

    /**
     * 根据键获取boolean
     *
     * @param key
     * @param processData
     * @return
     */
    internal fun getBoolean(key: String, processData: ProcessData): Boolean? {
        return Coerce.toBoolean((metaData.get(key) ?: processData[key] ?: return null).analysis())
    }

    /**
     * 根据键获取double
     *
     * @param key
     * @param processData
     * @return
     */
    internal fun getDouble(key: String, processData: ProcessData): Double? {
        return Coerce.toDouble((metaData.get(key) ?: processData[key] ?: return null).analysis())
    }

    /**
     * 根据键获取float
     *
     * @param key
     * @param processData
     * @return
     */
    internal fun getFloat(key: String, processData: ProcessData): Float? {
        return Coerce.toFloat((metaData.get(key) ?: processData[key] ?: return null).analysis())
    }

    /**
     * 根据键获取long
     *
     * @param key
     * @param processData
     * @return
     */
    internal fun getLong(key: String, processData: ProcessData): Long? {
        return Coerce.toLong((metaData.get(key) ?: processData[key] ?: return null).analysis())
    }

    /**
     * 根据键获取int
     *
     * @param key
     * @param processData
     * @return
     */
    internal fun getInt(key: String, processData: ProcessData): Int? {
        return Coerce.toInteger((metaData.get(key) ?: processData[key] ?: return null).analysis())
    }

    /**
     * 根据键获取string
     *
     * @param key
     * @param processData
     * @return
     */
    internal fun getString(key: String, processData: ProcessData): String? {
        return (metaData.get(key) ?: processData[key] ?: return null).analysis().toString()
    }
}