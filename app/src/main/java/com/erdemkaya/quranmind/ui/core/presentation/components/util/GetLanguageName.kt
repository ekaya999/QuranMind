package com.erdemkaya.quranmind.ui.core.presentation.components.util

fun getLanguageName(languageCode: String): String {
    return when (languageCode) {
        "tr" -> "Türkçe"
        "en" -> "English"
        "ar" -> "Arapça"
        "sq" -> "Arnavutça"
        "az" -> "Azeri Türkçesi"
        "bn" -> "Bengalce"
        "bs" -> "Boşnakça"
        "bg" -> "Bulgarca"
        "zh" -> "Çince"
        else -> languageCode
    }
}