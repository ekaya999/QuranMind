package com.erdemkaya.quranmind.ui.core.presentation.components.util

import java.text.Normalizer
import java.util.Locale

fun normalize(text: String): String {
    return Normalizer
        .normalize(text, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .lowercase(Locale.getDefault())
}

fun String.normalizeForSorting(): String {
    return Normalizer
        .normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .lowercase(Locale.getDefault())
}