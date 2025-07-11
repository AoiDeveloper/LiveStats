package net.tsubu.liveStats.api

import kotlinx.serialization.builtins.serializer
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object StatsProperties {
    val LOGIN_COUNT = PropertyKey(PropertyKey.Identifier.of("LiveStats", "login_count"), Int.serializer(), 0).register()
    val LAST_LOGIN =
        PropertyKey(
            PropertyKey.Identifier.of("LiveStats", "last_login_timestamp"),
            Instant.serializer(),
            Instant.DISTANT_PAST,
        ).register()

    private val allKeys = mutableListOf<PropertyKey<*>>()

    fun register(key: PropertyKey<*>) {
        allKeys.add(key)
    }

    fun findByName(name: String): PropertyKey<*>? =
        allKeys.find {
            it.name.toString() == name
        }
}

fun <T> PropertyKey<T>.register(): PropertyKey<T> = this.also { StatsProperties.register(this) }
