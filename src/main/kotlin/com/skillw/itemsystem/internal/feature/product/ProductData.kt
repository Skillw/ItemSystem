package com.skillw.itemsystem.internal.feature.product

import com.skillw.itemsystem.api.builder.BaseItemBuilder
import com.skillw.itemsystem.internal.core.builder.ProcessData
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.common5.Demand
import taboolib.common5.mirrorNow

/**
 * @className ProductData
 *
 * @author Glom
 * @date 2022/8/14 10:14 Copyright 2022 user. All rights reserved.
 */
data class ProductData(val items: List<ItemStack>, val isSame: Boolean, val demand: Demand = Demand("")) {

    companion object {
        @JvmStatic
        fun product(
            item: BaseItemBuilder,
            demandStr: String,
            entity: LivingEntity?,
        ): ProductData {
            val (product, amount, isSame, data, demand) = DemandData.demand(demandStr, entity)
            if (!product) return ProductData(emptyList(), false)
            return mirrorNow("product-with-data") {
                val buildData = fun(): ProcessData {
                    return ProcessData(entity) {
                        context.apply {
                            data.forEach { (key, value) ->
                                when (value) {
                                    is Map<*, *> -> put(key, value.toMutableMap())
                                    else -> put(key, value)
                                }
                            }
                        }
                    }
                }
                if (isSame) {
                    val result = item.build(entity, buildData())
                    return@mirrorNow ProductData(List(amount) { result }, true, demand)
                }
                val processData = buildData()
                ProductData(List(amount) { item.build(entity, processData.clone()) }, false, demand)
            }
        }
    }
}