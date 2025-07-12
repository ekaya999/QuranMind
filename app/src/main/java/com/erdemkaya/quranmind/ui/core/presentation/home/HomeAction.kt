package com.erdemkaya.quranmind.ui.core.presentation.home

import androidx.compose.ui.graphics.ImageBitmap

sealed interface HomeAction {
    data object OnShareClick: HomeAction
    data object OnSaveClick: HomeAction
    data class OnBitmapCreated(val bitmap: ImageBitmap): HomeAction
    data object OnQuranClick: HomeAction
    data object OnDuaClick: HomeAction
    data object OnProfileClick: HomeAction
}