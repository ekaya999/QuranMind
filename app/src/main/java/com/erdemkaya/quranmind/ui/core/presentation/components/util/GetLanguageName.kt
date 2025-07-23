package com.erdemkaya.quranmind.ui.core.presentation.components.util

import android.content.Context
import com.erdemkaya.quranmind.R

fun getLanguageName(context: Context, languageCode: String): String {
    return when (languageCode) {
        "tr" -> context.getString(R.string.turkish)
        "en" -> context.getString(R.string.english)
        "ar" -> context.getString(R.string.arabic)
        "sq" -> context.getString(R.string.albanian)
        "az" -> context.getString(R.string.azerbaidjani)
        "bn" -> context.getString(R.string.bengal)
        "bs" -> context.getString(R.string.bosnian)
        "bg" -> context.getString(R.string.bulgarian)
        "zh" -> context.getString(R.string.chinese)
        "de" -> context.getString(R.string.german)
        else -> languageCode
    }
}