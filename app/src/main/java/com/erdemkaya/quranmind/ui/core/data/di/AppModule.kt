package com.erdemkaya.quranmind.ui.core.data.di

import com.erdemkaya.quranmind.QuranMindApp
import com.erdemkaya.quranmind.ui.core.data.di.home.HomeRepositoryImpl
import com.erdemkaya.quranmind.ui.core.domain.home.HomeRepository
import com.erdemkaya.quranmind.ui.core.presentation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single<CoroutineScope> {
        (androidApplication() as QuranMindApp).applicationScope
    }

    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()

    viewModelOf(::HomeViewModel)
}