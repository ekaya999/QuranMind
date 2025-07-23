package com.erdemkaya.quranmind.ui.core.presentation.components.util

fun getTranslatorFromFile(file: String): String {
    val fileNameLower = file.lowercase()
    return when {
        fileNameLower.contains("diyanet") -> "Diyanet İşleri"
        fileNameLower.contains("yazir") -> "Elmalılı Hamdi Yazır"
        fileNameLower.contains("ozturk") -> "Yaşar Nuri Öztürk"
        fileNameLower.contains("mammadaliyev") -> "Vasim Məmmədəliyev ve Ziya Bünyadov"
        fileNameLower.contains("musayev") -> "Əlixan Musayev"
        fileNameLower.contains("theophanov") -> "Цветан Теофанов"
        fileNameLower.contains("hoque") -> "জহূরুল হক"
        fileNameLower.contains("muhiuddin") -> "মুহিউদ্দিন খান"
        fileNameLower.contains("korkut") -> "Besim Korkut"
        fileNameLower.contains("mlivo") -> "Mustafa Mlivo"
        fileNameLower.contains("ahmeti") -> "Sherif Ahmeti"
        fileNameLower.contains("mehdiu") -> "Feti Mehdiu"
        fileNameLower.contains("nahi") -> "Hasan Efendi Nahi"
        fileNameLower.contains("jian") -> "馬堅"
        fileNameLower.contains("aburida") -> "Abu Rida Muhammad ibn Ahmad ibn Rasul"
        else -> file.substringBeforeLast(".xml").replace("quran-tr-", "").capitalize()
    }
}