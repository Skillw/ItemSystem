package com.skillw.itemsystem.internal.feature.effect

import org.bukkit.entity.Item
import taboolib.common.platform.ProxyParticle
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.sendTo
import taboolib.common.util.Location
import taboolib.common.util.Vector
import taboolib.common.util.random
import taboolib.module.effect.ParticleObj
import taboolib.module.effect.ParticleSpawner
import taboolib.module.effect.shape.Arc
import taboolib.platform.util.toProxyLocation
import java.util.*

class RandomItemEffect(val origin: Location) {
    val items = LinkedList<Item>()
    private val spawner = object : ParticleSpawner {
        fun ProxyParticle.send(location: Location) {
            sendTo(
                location = location,
                offset = Vector.getRandom(),
                data = if (this == ProxyParticle.REDSTONE) dustData.random() else null
            )
        }

        override fun spawn(location: Location) {
            items.forEach { item ->
                particles.random().send(item.location.toProxyLocation())
            }
        }
    }
    private val list = listOf(Arc(origin, spawner))


    fun play() {
        var count = 0
        submitAsync(period = 1) {
            items.removeIf { it.isDead || it.isOnGround || it.isInWater }
            list.forEach(ParticleObj::show)
            if (count++ >= 100 || items.isEmpty()) return@submitAsync
        }
    }

    companion object {
        private val dustData = HashSet<ProxyParticle.DustData>().apply {
            repeat(10) {
                add(ProxyParticle.DustData(java.awt.Color(random(0, 255), random(0, 255), random(0, 255)), 1f))
            }
        }

        private val particles = setOf(ProxyParticle.REDSTONE)

    }
}
