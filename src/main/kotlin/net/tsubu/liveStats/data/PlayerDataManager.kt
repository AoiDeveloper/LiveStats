package net.tsubu.liveStats.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import net.tsubu.liveStats.LiveStats
import net.tsubu.liveStats.achievement.AchievementManager
import net.tsubu.liveStats.achievement.PlayerAchievementChecker
import net.tsubu.liveStats.db.DatabaseManager
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class PlayerDataManager(
    private val plugin: LiveStats,
    private val dbManager: DatabaseManager,
    private val achievementManager: AchievementManager,
) {
    private val playerDataCache = ConcurrentHashMap<UUID, PlayerData>()
    private val playerScopes = ConcurrentHashMap<UUID, CoroutineScope>()

    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        managerScope.launch {
            dbManager.setupDatabase()
        }
    }

    fun onPlayerJoin(uuid: UUID) {
        val playerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        playerScopes[uuid] = playerScope

        managerScope.launch {
            val loadedData = dbManager.loadPlayerData(uuid) ?: PlayerData(uuid)
            playerDataCache[uuid] = loadedData

            val checker = PlayerAchievementChecker(loadedData, achievementManager, playerScope)
            checker.start()
        }
    }

    fun onPlayerJoin(player: Player) = onPlayerJoin(player.uniqueId)

    fun onPlayerQuit(uuid: UUID) {
        managerScope.launch {
            playerDataCache[uuid]?.let { dbManager.savePlayerData(it) }
            playerDataCache.remove(uuid)
        }
        playerScopes.remove(uuid)?.cancel()
    }

    fun onPlayerQuit(player: Player) = onPlayerQuit(player.uniqueId)

    fun getPlayerData(uuid: UUID): PlayerData? = playerDataCache[uuid]

    fun getPlayerScope(uuid: UUID): CoroutineScope? = playerScopes[uuid]
}
