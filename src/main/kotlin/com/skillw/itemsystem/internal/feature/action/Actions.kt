package com.skillw.itemsystem.internal.feature.action

import com.skillw.itemsystem.api.action.ActionType
import com.skillw.pouvoir.api.annotation.AutoRegister

/**
 * @className Actions
 *
 * @author Glom
 * @date 2022/8/19 8:14 Copyright 2022 user. All rights reserved.
 */

@AutoRegister
val left = ActionType("left")

@AutoRegister
val right = ActionType("right")

@AutoRegister
val shiftRight = ActionType("shift_right")

@AutoRegister
val shiftLeft = ActionType("shift_left")

@AutoRegister
val clickItem = ActionType("click_item")