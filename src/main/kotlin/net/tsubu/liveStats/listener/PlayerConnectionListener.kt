package net.tsubu.liveStats.listener

import net.tsubu.liveStats.data.PlayerDataManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerConnectionListener(
    private val playerDataManager: PlayerDataManager,
) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerJoin(event: AsyncPlayerPreLoginEvent) {
        playerDataManager.onPlayerJoin(event.uniqueId)
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        playerDataManager.onPlayerQuit(event.player)
    }
}
