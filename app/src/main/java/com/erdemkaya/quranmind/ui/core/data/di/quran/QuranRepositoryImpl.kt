package com.erdemkaya.quranmind.ui.core.data.di.quran

import com.erdemkaya.quranmind.ui.core.domain.quran.QuranRepository

import com.erdemkaya.quranmind.ui.core.database.dao.quran.QuranDao
import com.erdemkaya.quranmind.ui.core.database.dao.quran.TranslationInfo
import com.erdemkaya.quranmind.ui.core.database.mappers.quran.VerseMappers.toDomain
import com.erdemkaya.quranmind.ui.core.domain.quran.Translation
import com.erdemkaya.quranmind.ui.core.domain.quran.VerseWithTranslations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuranRepositoryImpl(
    private val quranDao: QuranDao
) : QuranRepository {

    override fun getVersesWithTranslations(surahNumber: Int): Flow<List<VerseWithTranslations>> = flow {
        val versesWithTranslations = quranDao.getVersesWithTranslations(surahNumber)
        emit(versesWithTranslations.map { it.toDomain() })
    }

    override fun getTranslation(verseId: Int, language: String): Flow<Translation?> = flow {
        val translationEntity = quranDao.getTranslation(verseId, language)
        emit(translationEntity?.toDomain())
    }

    override suspend fun hasTranslation(
        language: String,
        translator: String
    ): Boolean {
        val hasTranslation = quranDao.hasTranslation(language, translator)
        return hasTranslation
    }


    suspend fun getAvailableTranslations(): List<TranslationInfo> {
        return quranDao.getAvailableTranslations()
    }


}