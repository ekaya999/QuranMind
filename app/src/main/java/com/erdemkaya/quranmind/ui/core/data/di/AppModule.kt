package com.erdemkaya.quranmind.ui.core.data.di

import com.erdemkaya.quranmind.QuranMindApp
import com.erdemkaya.quranmind.ui.core.presentation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single<CoroutineScope> {
        (androidApplication() as QuranMindApp).applicationScope
    }

    viewModelOf(::HomeViewModel)
    
}