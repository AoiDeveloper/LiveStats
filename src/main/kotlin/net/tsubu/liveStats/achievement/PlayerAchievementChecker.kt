package net.tsubu.liveStats.achievement

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import net.tsubu.liveStats.api.PropertyAccessor
import net.tsubu.liveStats.api.PropertyKey
import net.tsubu.liveStats.data.PlayerData

class PlayerAchievementChecker(
    private val playerData: PlayerData,
    private val achievementManager: AchievementManager,
    private val playerScope: CoroutineScope,
) {
    fun start() {
        val allAchievements = achievementManager.getAllRegistered()

        allAchievements.forEach { registeredAchievement ->
            if (!playerData.hasAchieved(registeredAchievement.achievement)) {
                setupListenerFor(registeredAchievement)
            }
        }
    }

    private fun setupListenerFor(registered: RegisteredAchievement) {
        val flowsToWatch =
            registered.requiredProperties.map { key ->
                playerData.getProperty(key).flow
            }

        combine(flowsToWatch) { latestValues ->
            val propsMap = registered.requiredProperties.zip(latestValues).toMap()

            val accessor =
                object : PropertyAccessor {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T> get(key: PropertyKey<T>): T = propsMap[key] as? T ?: key.defaultValue
                }

            registered.condition(accessor)
        }.filter { isSatisfied -> isSatisfied }
            .take(1)
            .onEach {
                val achievement = registered.achievement
                playerData.unlockAchievement(achievement)
            }.launchIn(playerScope)
    }
}
