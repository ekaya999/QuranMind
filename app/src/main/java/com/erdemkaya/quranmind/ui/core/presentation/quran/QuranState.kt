package com.erdemkaya.quranmind.ui.core.presentation.quran

import com.erdemkaya.quranmind.ui.core.database.dao.quran.TranslationInfo
import com.erdemkaya.quranmind.ui.core.presentation.quran.model.VerseUiModel

data class QuranState(
    val verses: List<VerseUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTranslations: Set<String> = emptySet(),
    val availableTranslations: List<TranslationInfo> = emptyList(),
    val currentSurahNumber: Int = 1,
    val searchQuery: String = "",
    val searchResults: List<VerseUiModel> = emptyList(),
    val selectedTranslation: String = "",
    val selectedLanguage: String = "",
    val selectedAyahNumber: Int? = null,
)