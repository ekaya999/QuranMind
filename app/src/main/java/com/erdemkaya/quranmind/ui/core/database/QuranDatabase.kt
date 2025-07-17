package com.erdemkaya.quranmind.ui.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.erdemkaya.quranmind.ui.core.database.dao.quran.QuranDao
import com.erdemkaya.quranmind.ui.core.database.entity.quran.TranslationEntity
import com.erdemkaya.quranmind.ui.core.database.entity.quran.VerseEntity

@Database(
    entities = [
        VerseEntity::class,
        TranslationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class QuranDatabase : RoomDatabase() {
    abstract fun quranDao(): QuranDao
}