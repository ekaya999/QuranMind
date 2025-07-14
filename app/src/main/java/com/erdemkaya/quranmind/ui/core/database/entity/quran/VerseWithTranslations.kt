package com.erdemkaya.quranmind.ui.core.database.entity.quran

import androidx.room.Embedded
import androidx.room.Relation

data class VerseWithTranslations (
    @Embedded val verse: Verse,
    @Relation(
        parentColumn = "id",
        entityColumn = "verseId"
    )
    val translations: List<Translation>
)