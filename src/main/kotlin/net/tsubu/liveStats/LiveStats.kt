package net.tsubu.liveStats

import kotlinx.coroutines.asCoroutineDispatcher
import net.tsubu.liveStats.di.appModule
import net.tsubu.liveStats.listener.PlayerConnectionListener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class LiveStats : JavaPlugin() {
    companion object {
        lateinit var instance: LiveStats
        lateinit var mainDispatcher: kotlinx.coroutines.CoroutineDispatcher
    }

    private val playerConnectionListener: PlayerConnectionListener by inject(PlayerConnectionListener::class.java)

    override fun onEnable() {
        instance = this
        mainDispatcher = server.scheduler.getMainThreadExecutor(this).asCoroutineDispatcher()

        startKoin {
            modules(
                module { single { this@LiveStats } },
                appModule,
            )
        }

        server.pluginManager.registerEvents(playerConnectionListener, this)
    }

    override fun onDisable() {
        stopKoin()
    }
}
