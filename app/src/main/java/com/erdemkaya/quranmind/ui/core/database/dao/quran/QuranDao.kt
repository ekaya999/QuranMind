package com.erdemkaya.quranmind.ui.core.database.dao.quran

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.erdemkaya.quranmind.ui.core.database.entity.quran.TranslationEntity
import com.erdemkaya.quranmind.ui.core.database.entity.quran.VerseEntity
import com.erdemkaya.quranmind.ui.core.database.entity.quran.VerseWithTranslationsEntity

@Dao
interface QuranDao {
    @Transaction
    @Query(
        """
       SELECT * FROM verses WHERE surahNumber = :surahNumber 
    """
    )
    suspend fun getVersesWithTranslations(surahNumber: Int): List<VerseWithTranslationsEntity>

    @Query(
        """
        SELECT * FROM translations 
        WHERE verseId = :verseId AND languageCode = :language
    """
    )
    suspend fun getTranslation(verseId: Int, language: String): TranslationEntity?

    @Query("SELECT * FROM translations WHERE translatorName = :translatorName LIMIT 1")
    suspend fun getTranslationByTranslator(translatorName: String): TranslationEntity?


    @Query(
        """
        SELECT DISTINCT languageCode, translatorName 
        FROM translations 
        ORDER BY languageCode
    """
    )
    suspend fun getAvailableTranslations(): List<TranslationInfo>

    @Query("SELECT COUNT(*) > 0 FROM translations WHERE languageCode = :lang AND translatorName = :translator")
    suspend fun hasTranslation(lang: String, translator: String): Boolean

    @Insert
    suspend fun insertVerses(verses: List<VerseEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTranslations(translations: List<TranslationEntity>)
}

data class TranslationInfo(
    val languageCode: String, val translatorName: String
)