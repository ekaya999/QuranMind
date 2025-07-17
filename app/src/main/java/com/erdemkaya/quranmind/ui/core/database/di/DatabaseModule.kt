package com.erdemkaya.quranmind.ui.core.database.di

import androidx.room.Room
import com.erdemkaya.quranmind.ui.core.database.DatabaseInitializer
import com.erdemkaya.quranmind.ui.core.database.QuranDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    // Database instance
    single {
        Room.databaseBuilder(
            androidApplication(),
            QuranDatabase::class.java,
            "quran_database"
        )
            .fallbackToDestructiveMigration() // Be careful with this in production
            .build()
    }

    // DAOs
    single { get<QuranDatabase>().quranDao() }
    single { DatabaseInitializer(androidApplication(), get()) }
}