package com.skillw.itemsystem.api

import com.skillw.itemsystem.ItemSystem.itemBuilderManager
import com.skillw.itemsystem.internal.builder.ProcessData
import com.skillw.itemsystem.internal.feature.ItemSyncer.syncNBT
import com.skillw.itemsystem.internal.feature.ItemUpdater.updateItem
import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

@ScriptTopLevel
object ItemAPI {

    /**
     * 生成一个ItemSystem的物品
     *
     * @param item String 物品构建器id
     * @param entity LivingEntity? 物品所在的实体
     * @param consumer Consumer<ProcessData> 操作过程数据
     * @return ItemStack? 结果物品
     */
    @JvmStatic
    fun productItem(
        item: String,
        entity: LivingEntity? = null,
        consumer: Consumer<ProcessData>? = null,
    ): ItemStack? {
        return itemBuilderManager[item]?.build(entity, ProcessData(entity).apply { consumer?.accept(this) })
    }

    /**
     * 同步物品的lore和NBT (会直接改变物品本身)
     *
     * @param entity LivingEntity 实体
     * @receiver ItemStack 物品
     */
    @JvmStatic
    fun ItemStack.syncLoreNBT(entity: LivingEntity) {
        this.syncNBT(entity)
    }

    /**
     * 更新物品
     *
     * @param entity LivingEntity 实体
     * @param variables Set<String> 只更新的变量名，如果含 ’all‘ 则更新所有
     * @param productData Map<String, Any> 构造数据(就是变量名和变量值)
     * @param force 是否强制更新物品
     * @return ItemStack 更新后的物品
     * @receiver ItemStack 待更新的物品
     */
    @JvmStatic
    fun ItemStack.update(
        entity: LivingEntity,
        variables: Set<String> = emptySet(),
        productData: Map<String, Any> = emptyMap(),
        force: Boolean = false,
    ): ItemStack {
        return this.updateItem(entity, variables, productData, force)
    }
}