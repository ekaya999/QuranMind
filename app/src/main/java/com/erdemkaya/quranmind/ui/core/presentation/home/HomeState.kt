package com.erdemkaya.quranmind.ui.core.presentation.home

import androidx.compose.ui.graphics.ImageBitmap

data class HomeState (
    val textArabic: String = "",
    val textTurkish: String = "",
    val quranSure: String = "",
    val imageBitmap: ImageBitmap? = null
)