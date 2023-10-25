package com.skillw.itemsystem.internal.feature.product

import com.skillw.itemsystem.util.GsonUtils.parseToMap
import com.skillw.pouvoir.util.calculate
import com.skillw.pouvoir.util.calculateDouble
import org.bukkit.entity.LivingEntity
import taboolib.common.util.random
import taboolib.common5.Coerce
import taboolib.common5.Demand
import kotlin.math.max

data class DemandData(
    val product: Boolean,
    val amount: Int,
    val isSame: Boolean,
    val data: Map<String, Any>,
    val demand: Demand,
) {
    companion object {
        @JvmStatic
        fun demand(demandStr: String, entity: LivingEntity? = null): DemandData {
            val demand = Demand("dem $demandStr")
            with(demand) {
                val amountFormula = get("amount", "1")!!
                val probableFormula = get("probable", "1")!!
                val data = get("data", "{}")!!
                val isSame = tags.contains("same")
                val product = random(probableFormula.calculateDouble(entity))
                val amount = max(1, Coerce.toInteger(amountFormula.calculate(entity)))
                return DemandData(product, amount, isSame, data = data.parseToMap(), demand)
            }
        }
    }
}
