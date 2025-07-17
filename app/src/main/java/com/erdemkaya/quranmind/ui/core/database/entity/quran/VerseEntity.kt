package com.erdemkaya.quranmind.ui.core.database.entity.quran

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verses")
data class VerseEntity (
    @PrimaryKey val id: Int,
    val surahNumber: Int,
    val verseNumber: Int,
    val arabicText: String,
)
