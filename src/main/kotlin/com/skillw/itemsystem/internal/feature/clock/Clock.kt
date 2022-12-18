package com.skillw.itemsystem.internal.feature.clock

internal object Clock {
    val currentTime: Long
        get() = System.currentTimeMillis()

    fun Long.isPast(): Boolean {
        return currentTime > this
    }
}