package com.skillw.itemsystem.internal.feature.compat.pouvoir.function

import com.skillw.itemsystem.internal.core.builder.ProcessData
import com.skillw.itemsystem.internal.feature.ItemDynamic.addDynamic
import com.skillw.itemsystem.internal.manager.ISConfig.unknownDynamic
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData

@AutoRegister
object FunctionDynamic : PouFunction<String>("dynamic", namespace = "item_system") {

    override fun execute(parser: Parser): String = parser.run{ return addDynamic(splitTill("{", "}")) }



}
