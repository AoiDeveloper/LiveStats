package net.tsubu.liveStats.data

import net.tsubu.liveStats.api.Achievement
import net.tsubu.liveStats.api.PropertyKey
import net.tsubu.liveStats.event.PlayerUnlockedAchievementEvent
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class PlayerData(
    val uuid: UUID,
) {
    private val properties = ConcurrentHashMap<PropertyKey<*>, PlayerProperty<*>>()
    private val unlockedAchievements = mutableSetOf<Achievement>()

    @Suppress("UNCHECKED_CAST")
    fun <T> getProperty(key: PropertyKey<T>): PlayerProperty<T> {
        val prop = properties.getOrPut(key) { PlayerProperty(key.defaultValue) }
        return prop as PlayerProperty<T>
    }

    fun getAllProperties(): Map<PropertyKey<*>, PlayerProperty<*>> = properties.toMap()

    fun hasAchieved(achievement: Achievement): Boolean = unlockedAchievements.contains(achievement)

    fun unlockAchievement(achievement: Achievement) {
        unlockedAchievements.add(achievement)
        val event = PlayerUnlockedAchievementEvent(achievement)
        event.callEvent()
    }

    fun getUnlockedAchievements(): Set<Achievement> = unlockedAchievements.toSet()

    fun setUnlockedAchievements(achievements: Set<Achievement>) {
        unlockedAchievements.clear()
        unlockedAchievements.addAll(achievements)
    }
}
