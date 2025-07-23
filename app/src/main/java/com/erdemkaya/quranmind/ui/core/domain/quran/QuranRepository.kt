package com.erdemkaya.quranmind.ui.core.domain.quran

import kotlinx.coroutines.flow.Flow

interface QuranRepository {
    fun getVersesWithTranslations(surahNumber: Int): Flow<List<VerseWithTranslations>>
    fun getTranslation(verseId: Int, language: String): Flow<Translation?>
    suspend fun hasTranslation(language: String, translator: String): Boolean
}