package com.erdemkaya.quranmind.ui.core.database.entity.quran

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "translations",
    foreignKeys = [ForeignKey(
        entity = VerseEntity::class,
        parentColumns = ["id"],
        childColumns = ["verseId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(
            value = ["verseId", "languageCode","translatorName"], unique = true)]
)

data class TranslationEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val verseId: Int,
    val languageCode: String,
    val translatorName: String,
    val translationText: String,
)