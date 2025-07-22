package net.tsubu.liveStats.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import net.tsubu.liveStats.achievement.AchievementManager
import net.tsubu.liveStats.data.PlayerDataManager
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object StatsAPI : KoinComponent {
    val playerDataManager: PlayerDataManager by inject()
    val achievementManager: AchievementManager by inject()

    /**
     * Retrieves the dedicated coroutine scope associated with a specific player.
     *
     * This function uses the player's unique identifier to get their coroutine scope,
     * which can be used for tasks such as managing asynchronous operations specific to the player.
     *
     * @param player The player whose coroutine scope is to be retrieved.
     * @return The coroutine scope associated with the player, or null if no scope exists.
     */
    fun getPlayerScope(player: Player): CoroutineScope? = playerDataManager.getPlayerScope(player.uniqueId)

    fun <T> getPropertyFlow(
        player: Player,
        key: PropertyKey<T>,
    ): StateFlow<T>? = playerDataManager.getPlayerData(player.uniqueId)?.getProperty(key)?.flow

    fun <T> setProperty(
        player: Player,
        key: PropertyKey<T>,
        value: T,
    ) {
        playerDataManager.getPlayerData(player.uniqueId)?.getProperty(key)?.update(value)
    }

    fun registerAchievement(
        achievement: Achievement,
        requiredProperties: List<PropertyKey<*>>,
        condition: (accessor: PropertyAccessor) -> Boolean,
    ) {
        achievementManager.register(achievement, requiredProperties, condition)
    }
}
