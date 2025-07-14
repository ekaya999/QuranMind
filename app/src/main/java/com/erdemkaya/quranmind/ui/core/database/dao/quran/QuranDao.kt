package com.erdemkaya.quranmind.ui.core.database.dao.quran

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.erdemkaya.quranmind.ui.core.database.entity.quran.Translation
import com.erdemkaya.quranmind.ui.core.database.entity.quran.Verse
import com.erdemkaya.quranmind.ui.core.database.entity.quran.VerseWithTranslations

interface QuranDao {
    @Transaction
    @Query("""
       SELECT * FROM verses WHERE surahNumber = :surahNumber 
    """)
    suspend fun getVersesWithTranslations(surahNumber: Int): List<VerseWithTranslations>

    @Query("""
        SELECT * FROM translations 
        WHERE verseId = :verseId AND languageCode = :language
    """)
    suspend fun getTranslation(verseId: Int, language: String): Translation?

    @Query("""
        SELECT DISTINCT languageCode, translatorName 
        FROM translations 
        ORDER BY languageCode
    """)
    suspend fun getAvailableTranslations(): List<TranslationInfo>

    @Insert
    suspend fun insertVerses(verses: List<Verse>)

    @Insert
    suspend fun insertTranslations(translations: List<Translation>)
}

data class TranslationInfo(
    val languageCode: String,
    val translatorName: String
)