package com.skillw.itemsystem.api.builder

import com.skillw.itemsystem.internal.builder.ProcessData
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.platform.util.ItemBuilder

/**
 * @className IProcessData
 *
 * @author Glom
 * @date 2022/8/14 7:46 Copyright 2022 user. All rights reserved.
 */
interface IProcessData {
    /** 实体 */
    val entity: LivingEntity?

    /** TLib的物品构建器，与本插件的区分开 */
    val builder: ItemBuilder

    /** 物品NBT */
    val nbt: ItemTag

    /** 会保存到 ITEM_SYSTEM.data 的变量名 */
    val savingKeys: MutableSet<String>

    /**
     * 解析对象 会解析其中的内联函数
     *
     * 可解析类型: String , Map , List
     *
     * 若类型不能被解析，则返回原类型
     *
     * @return 解析结果
     */
    fun <T> T.analysis(): T

    /**
     * 执行内联函数
     *
     * @return 结果
     */
    fun String.eval(): Any

    /**
     * 获取结果
     *
     * @param receiver 操作过程数据
     * @return 结果
     */
    fun result(receiver: ProcessData.() -> Unit): ItemStack
}