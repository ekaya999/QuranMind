package com.erdemkaya.quranmind

import android.app.Application
import com.erdemkaya.quranmind.ui.core.data.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
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
            modules(appModule)
        }
    }
}