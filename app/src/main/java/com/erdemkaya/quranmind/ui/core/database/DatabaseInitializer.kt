// DatabaseInitializer.kt - Fixed version
package com.erdemkaya.quranmind.ui.core.database

import android.content.Context
import com.erdemkaya.quranmind.ui.core.database.dao.quran.QuranDao
import com.erdemkaya.quranmind.ui.core.database.entity.quran.TranslationEntity
import com.erdemkaya.quranmind.ui.core.database.entity.quran.VerseEntity
import com.erdemkaya.quranmind.ui.core.presentation.components.util.getTranslatorFromFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.util.Locale

class DatabaseInitializer(
    private val context: Context, private val quranDao: QuranDao
) {
    fun initializeDatabase(onComplete: (() -> Unit)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val xmlFiles = context.assets.list("")?.filter { it.endsWith(".xml") }

                // Check if verses are already loaded
                val existingVerses = quranDao.getVersesWithTranslations(1)
                if (existingVerses.isEmpty()) {
                    loadArabicVerses(xmlFiles)
                }

                // Load only default translations (e.g., Arabic and one Turkish)
                loadDefaultTranslations(xmlFiles)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                withContext(Dispatchers.Main) {
                    onComplete?.invoke()
                }
            }
        }
    }
    // Neue Methode: Lade eine spezifische Übersetzung on-demand
    suspend fun loadSpecificTranslation(fileName: String, lang: String, translator: String, onComplete: () -> Unit) {
        val verseMap = getVerseMap()
        try {
            loadTranslationFromXml(fileName, lang, translator, verseMap)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            onComplete()
        }
    }

    private suspend fun loadDefaultTranslations(xmlFiles: List<String>?) {
        val verseMap = getVerseMap()
        // Arabisch immer laden
        val arabicFile = xmlFiles?.find { it.contains("arabic", ignoreCase = true) }
        if (arabicFile != null) {
            val lang = "ar"
            val translator = getTranslatorFromFile(arabicFile)
            if (!quranDao.hasTranslation(lang, translator)) {
                loadTranslationFromXml(arabicFile, lang, translator, verseMap)
            }
        }
        // Gerätesprache
        val deviceLang = Locale.getDefault().language
        val localLangFiles = xmlFiles?.filter { it.startsWith("${deviceLang}_") } ?: emptyList()
        for (file in localLangFiles) {
            val translator = getTranslatorFromFile(file)
            if (!quranDao.hasTranslation(deviceLang, translator)) {
                loadTranslationFromXml(file, deviceLang, translator, verseMap)
            }
        }
    }

    private suspend fun loadArabicVerses(xmlFiles: List<String>?) {
        val arabicFile = xmlFiles?.find {
            it.contains("arabic", ignoreCase = true) || (it.contains(
                "quran", ignoreCase = true
            ) && !it.contains("tr", ignoreCase = true))
        }

        if (arabicFile != null) {
            val verses = loadVersesFromXml(arabicFile)
            quranDao.insertVerses(verses)
        }
    }

//    private suspend fun loadAllTranslations(xmlFiles: List<String>?) {
//        val turkishFiles = xmlFiles?.filter {
//            it.contains("tr", ignoreCase = true)
//        } ?: emptyList()
//
//        val arabicFiles = xmlFiles?.filter {
//            it.contains("arabic", ignoreCase = true)
//        } ?: emptyList()
//
//        val verseMap = mutableMapOf<Pair<Int, Int>, Int>() // (surah, verse) -> id
//
//        for (surah in 1..114) {
//            val surahVerses = quranDao.getVersesWithTranslations(surah)
//            surahVerses.forEach { verseEntity ->
//                verseMap[verseEntity.verse.surahNumber to verseEntity.verse.verseNumber] =
//                    verseEntity.verse.id
//            }
//        }
//
//        turkishFiles.forEach { file ->
//            // First determine the translator name from the file
//            val fileNameLower = file.lowercase()
//            val translatorNameTurkish = when {
//                fileNameLower.contains("diyanet") -> "Diyanet İşleri"
//                fileNameLower.contains("yazir") -> "Elmalılı Hamdi Yazır"
//                fileNameLower.contains("ates") -> "Süleyman Ateş"
//                fileNameLower.contains("ozturk") -> "Yaşar Nuri Öztürk"
//                fileNameLower.contains("yildirim") -> "Suat Yıldırım"
//                fileNameLower.contains("meal") -> "Diyanet Meali"
//                fileNameLower.contains("golpinarli") -> "Abdülbaki Gölpınarlı"
//                fileNameLower.contains("bulac") -> "Ali Bulaç"
//                else -> file.substringBeforeLast(".xml").replace("quran-tr-", "")
//                    .replace("quran_tr_", "").replace("tr_", "").replace("-", " ").replace("_", " ")
//                    .split(" ").joinToString(" ") { it.capitalize() }
//            }
//            try {
//                loadTranslationFromXml(file, "tr", translatorNameTurkish, verseMap)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        arabicFiles.forEach { file ->
//            val fileNameLower = file.lowercase()
//            val translatorNameArabic = when {
//                fileNameLower.contains("arabic") -> "Arapça"
//                else -> file.substringBeforeLast(".xml")
//            }
//
//            try {
//                loadTranslationFromXml(file, "ar", translatorNameArabic, verseMap)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    private fun loadVersesFromXml(fileName: String): List<VerseEntity> {
        val verses = mutableListOf<VerseEntity>()
        var currentId = 1

        try {
            context.assets.open(fileName).use { inputStream ->
                val factory = XmlPullParserFactory.newInstance()
                val parser = factory.newPullParser()
                parser.setInput(inputStream, "UTF-8")

                var eventType = parser.eventType
                var currentSurahNumber = 0

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (parser.name) {
                                "sura" -> {
                                    currentSurahNumber =
                                        parser.getAttributeValue(null, "index")?.toIntOrNull() ?: 0
                                }

                                "aya" -> {
                                    val verseNumber =
                                        parser.getAttributeValue(null, "index")?.toIntOrNull() ?: 0
                                    val text = parser.getAttributeValue(null, "text") ?: ""

                                    if (currentSurahNumber > 0 && verseNumber > 0) {
                                        verses.add(
                                            VerseEntity(
                                                id = currentId++,
                                                surahNumber = currentSurahNumber,
                                                verseNumber = verseNumber,
                                                arabicText = text
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    eventType = parser.next()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return verses
    }

    private suspend fun loadTranslationFromXml(
        fileName: String,
        languageCode: String,
        translatorName: String,
        verseMap: Map<Pair<Int, Int>, Int>
    ) {
        val translations = mutableListOf<TranslationEntity>()

        try {
            context.assets.open(fileName).use { inputStream ->
                val factory = XmlPullParserFactory.newInstance()
                val parser = factory.newPullParser()
                parser.setInput(inputStream, "UTF-8")

                var eventType = parser.eventType
                var currentSurahNumber = 0

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (parser.name) {
                                "sura" -> {
                                    currentSurahNumber =
                                        parser.getAttributeValue(null, "index")?.toIntOrNull() ?: 0
                                }

                                "aya" -> {
                                    val verseNumber =
                                        parser.getAttributeValue(null, "index")?.toIntOrNull() ?: 0
                                    val text = parser.getAttributeValue(null, "text") ?: ""

                                    if (currentSurahNumber > 0 && verseNumber > 0 && text.isNotEmpty()) {
                                        val verseId = verseMap[currentSurahNumber to verseNumber]

                                        if (verseId != null) {
                                            translations.add(
                                                TranslationEntity(
                                                    verseId = verseId,
                                                    languageCode = languageCode,
                                                    translatorName = translatorName,
                                                    translationText = text
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    eventType = parser.next()
                }
            }

            // Insert translations in batches
            if (translations.isNotEmpty()) {
                translations.chunked(1000).forEach { batch ->
                    quranDao.insertTranslations(batch)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
    // Hilfsfunktion für verseMap (aus deinem Code extrahiert)
    private suspend fun getVerseMap(): Map<Pair<Int, Int>, Int> {
        val verseMap = mutableMapOf<Pair<Int, Int>, Int>()
        for (surah in 1..114) {
            val surahVerses = quranDao.getVersesWithTranslations(surah)
            surahVerses.forEach { verseEntity ->
                verseMap[verseEntity.verse.surahNumber to verseEntity.verse.verseNumber] =
                    verseEntity.verse.id
            }
        }
        return verseMap
    }


}