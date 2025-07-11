package net.tsubu.liveStats.event

import net.tsubu.liveStats.api.Achievement
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerUnlockedAchievementEvent(
    val achievement: Achievement,
) : Event(false) {
    companion object {
        @JvmStatic
        val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlerList
    }

    override fun getHandlers(): HandlerList = handlerList
}
