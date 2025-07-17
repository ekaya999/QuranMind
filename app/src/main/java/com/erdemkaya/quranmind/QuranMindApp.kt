package com.erdemkaya.quranmind

import android.app.Application
import com.erdemkaya.quranmind.ui.core.data.di.appModule
import com.erdemkaya.quranmind.ui.core.database.DatabaseInitializer
import com.erdemkaya.quranmind.ui.core.database.di.databaseModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class QuranMindApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@QuranMindApp)
            androidLogger()
            modules(appModule, databaseModule)
        }
        val databaseInitializer: DatabaseInitializer by inject()
        databaseInitializer.initializeDatabase()
    }
}