package com.erdemkaya.quranmind.ui.core.presentation.quran

sealed interface QuranAction {
    data object OnSelectTranslationClick : QuranAction
    data class OnToggleTranslation(val languageCode: String, val translatorName: String) :
        QuranAction

    data object OnHomeClick : QuranAction
    data object OnDuaClick : QuranAction
    data object OnProfileClick : QuranAction
    data class OnSearchSurahClick(val surahNumber: Int) : QuranAction
    data class OnSearchSurahAndAyahClick(val surahNumber: Int,val ayahNumber: Int) : QuranAction
    data class OnAddTranslation(val lang: String, val translator: String, val fileName: String) : QuranAction
    data object OnRemoveTranslation : QuranAction
}