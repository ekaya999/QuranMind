package com.erdemkaya.quranmind.ui.core.presentation.quran.model

data class VerseUiModel (
    val id: Int,
    val verseNumber: String,
    val ayahNumber: Int,
    val arabicText: String,
    val translations: List<TranslationUiModel>,
    val isBookmarked: Boolean

)