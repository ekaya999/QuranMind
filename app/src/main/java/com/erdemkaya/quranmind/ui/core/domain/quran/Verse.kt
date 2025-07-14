package com.erdemkaya.quranmind.ui.core.domain.quran

data class Verse (
    val id: Int,
    val surahNumber: Int,
    val verseNumber: Int,
    val arabicText: String
)