package net.tsubu.liveStats.di

import net.tsubu.liveStats.achievement.AchievementManager
import net.tsubu.liveStats.data.PlayerDataManager
import net.tsubu.liveStats.db.DatabaseManager
import net.tsubu.liveStats.listener.PlayerConnectionListener
import org.koin.dsl.module

val appModule =
    module {
        single { DatabaseManager(get()) }

        single { PlayerDataManager(get(), get(), get()) }

        single { AchievementManager() }

        single { PlayerConnectionListener(get()) }
    }
