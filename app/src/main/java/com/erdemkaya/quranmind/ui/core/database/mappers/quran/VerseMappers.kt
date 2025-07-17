package com.erdemkaya.quranmind.ui.core.database.mappers.quran

import com.erdemkaya.quranmind.ui.core.database.entity.quran.TranslationEntity
import com.erdemkaya.quranmind.ui.core.database.entity.quran.VerseEntity
import com.erdemkaya.quranmind.ui.core.database.entity.quran.VerseWithTranslationsEntity
import com.erdemkaya.quranmind.ui.core.domain.quran.Translation
import com.erdemkaya.quranmind.ui.core.domain.quran.Verse
import com.erdemkaya.quranmind.ui.core.domain.quran.VerseWithTranslations

object VerseMappers {

    fun VerseWithTranslationsEntity.toDomain(): VerseWithTranslations {
        return VerseWithTranslations(
            verse = Verse(
                id = verse.id,
                surahNumber = verse.surahNumber,
                verseNumber = verse.verseNumber,
                arabicText = verse.arabicText
            ),
            translation = translations.map { it.toDomain() },
            isFavorite = false
        )
    }

    fun Verse.toEntity(): VerseEntity {
        return VerseEntity(
            id = id,
            surahNumber = surahNumber,
            verseNumber = verseNumber,
            arabicText = arabicText
        )
    }

    fun TranslationEntity.toDomain(): Translation {
        return Translation(
            languageCode = languageCode,
            translatorName = translatorName,
            text = translationText
        )
    }

    fun Translation.toEntity(verseId: Int): TranslationEntity {
        return TranslationEntity(
            verseId = verseId,
            languageCode = languageCode,
            translatorName = translatorName,
            translationText = text
        )
    }
}
