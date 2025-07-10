package com.erdemkaya.quranmind.ui.core.data.di

import com.erdemkaya.quranmind.QuranMindApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single<CoroutineScope> {
        (androidApplication() as QuranMindApp).applicationScope
    }
}