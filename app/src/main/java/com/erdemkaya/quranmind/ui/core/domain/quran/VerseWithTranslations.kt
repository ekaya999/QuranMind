package com.erdemkaya.quranmind.ui.core.domain.quran

data class VerseWithTranslations (
    val verse: Verse,
    val translation: List<Translation>,
    val isFavorite: Boolean
)