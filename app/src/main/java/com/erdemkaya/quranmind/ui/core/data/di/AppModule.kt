package com.erdemkaya.quranmind.ui.core.data.di

import com.erdemkaya.quranmind.QuranMindApp
import com.erdemkaya.quranmind.ui.core.data.di.home.HomeRepositoryImpl
import com.erdemkaya.quranmind.ui.core.data.di.quran.QuranRepositoryImpl
import com.erdemkaya.quranmind.ui.core.database.DatabaseInitializer
import com.erdemkaya.quranmind.ui.core.domain.home.HomeRepository
import com.erdemkaya.quranmind.ui.core.domain.quran.QuranRepository
import com.erdemkaya.quranmind.ui.core.presentation.home.HomeViewModel
import com.erdemkaya.quranmind.ui.core.presentation.quran.QuranViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.*
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single<CoroutineScope> {
        (androidApplication() as QuranMindApp).applicationScope
    }

    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()
    singleOf(::QuranRepositoryImpl).bind<QuranRepository>()
    single { DatabaseInitializer(androidContext(), get() ) }


    viewModelOf(::HomeViewModel)
    viewModelOf(::QuranViewModel)
}