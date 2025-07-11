package net.tsubu.liveStats.achievement

import net.tsubu.liveStats.api.Achievement
import net.tsubu.liveStats.api.PropertyAccessor
import net.tsubu.liveStats.api.PropertyKey
import java.util.concurrent.ConcurrentHashMap

/**
 * Manages the registration and retrieval of achievements.
 *
 * This class provides functionality to register achievements along with their required properties
 * and conditions for unlocking. It also allows retrieval of all registered achievements.
 */
class AchievementManager {
    private val registered = ConcurrentHashMap<String, RegisteredAchievement>()

    fun register(
        achievement: Achievement,
        requiredProperties: List<PropertyKey<*>>,
        condition: (accessor: PropertyAccessor) -> Boolean,
    ) {
        val registeredAchievement = RegisteredAchievement(achievement, requiredProperties, condition)
        registered[achievement.id] = registeredAchievement
    }

    fun getAllRegistered(): List<RegisteredAchievement> = registered.values.toList()
}
