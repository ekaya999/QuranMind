package com.erdemkaya.quranmind.ui.core.presentation.components.util

import com.erdemkaya.quranmind.R

sealed class QuranMindNavBarItem(
    val icon: Int,
    val screenRoute: String,
    val title: String
) {
    object Home: QuranMindNavBarItem(R.drawable.crescent, "home", "Ana sayfa")
    object Quran: QuranMindNavBarItem(R.drawable.quran, "quran", "Kuran")
    object Dua: QuranMindNavBarItem(R.drawable.dua, "dua","Dua")
    object Profile: QuranMindNavBarItem(R.drawable.profile, "profile","Ben")
}