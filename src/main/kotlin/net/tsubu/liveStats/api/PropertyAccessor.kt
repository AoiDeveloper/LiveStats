package net.tsubu.liveStats.api

interface PropertyAccessor {
    operator fun <T> get(key: PropertyKey<T>): T
}
