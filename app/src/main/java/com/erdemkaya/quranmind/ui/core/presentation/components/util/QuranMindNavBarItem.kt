package com.erdemkaya.quranmind.ui.core.presentation.components.util

import androidx.annotation.StringRes
import androidx.compose.ui.platform.LocalContext
import com.erdemkaya.quranmind.R

sealed class QuranMindNavBarItem(
    val icon: Int,
    val screenRoute: String,
    @StringRes val title: Int
) {
    object Home: QuranMindNavBarItem(R.drawable.crescent, "home", R.string.home)
    object Quran: QuranMindNavBarItem(R.drawable.quran, "quran", R.string.quran)
    object Dua: QuranMindNavBarItem(R.drawable.dua, "dua",R.string.dua)
    object Profile: QuranMindNavBarItem(R.drawable.profile, "profile",R.string.profile)
}