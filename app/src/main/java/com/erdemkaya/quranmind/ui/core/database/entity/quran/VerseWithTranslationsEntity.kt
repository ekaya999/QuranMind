package com.erdemkaya.quranmind.ui.core.database.entity.quran

import androidx.room.Embedded
import androidx.room.Relation

data class VerseWithTranslationsEntity (
    @Embedded val verse: VerseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "verseId"
    )
    val translations: List<TranslationEntity>
)