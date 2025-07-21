package com.erdemkaya.quranmind.ui.core.presentation.quran.mapper

import com.erdemkaya.quranmind.ui.core.domain.quran.Translation
import com.erdemkaya.quranmind.ui.core.domain.quran.VerseWithTranslations
import com.erdemkaya.quranmind.ui.core.presentation.components.util.getLanguageName
import com.erdemkaya.quranmind.ui.core.presentation.quran.model.TranslationUiModel
import com.erdemkaya.quranmind.ui.core.presentation.quran.model.VerseUiModel

fun VerseWithTranslations.toUiModel(
    selectedTranslation: String
): VerseUiModel {
    return VerseUiModel(
        id = verse.id,
        verseNumber = "${verse.surahNumber}:${verse.verseNumber}",
        ayahNumber = verse.verseNumber,
        arabicText = verse.arabicText,
        translations = translation.filter { "${it.languageCode}_${it.translatorName}" in selectedTranslation }
            .map { it.toUiModel() },
        isBookmarked = isFavorite
    )

}

fun Translation.toUiModel(): TranslationUiModel {
    val language = getLanguageName(languageCode)
    return TranslationUiModel(
        text = text, translatorInfo = "$language - $translatorName", languageCode = languageCode
    )
}


