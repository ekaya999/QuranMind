package com.erdemkaya.quranmind.ui.core.presentation.components.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun getSurahName(number: Int, languageCode: String): String {
    val context = LocalContext.current
    val surahKey = "surah_name$number"

    // Erst versuchen, die gewünschte Sprache zu verwenden
    val localeContext = context.createLocaleContext(languageCode)
    val resIdLocale = localeContext.resources.getIdentifier(
        surahKey, "string", context.packageName
    )
    if (resIdLocale != 0) {
        return localeContext.getString(resIdLocale)
    }

    // Fallback: Gerätesprache
    val resIdDefault = context.resources.getIdentifier(
        surahKey, "string", context.packageName
    )
    return if (resIdDefault != 0) context.getString(resIdDefault) else ""
}

fun Context.createLocaleContext(languageCode: String): Context {
    val locale = Locale(languageCode)
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}