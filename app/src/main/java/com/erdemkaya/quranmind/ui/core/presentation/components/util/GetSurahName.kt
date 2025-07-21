package com.erdemkaya.quranmind.ui.core.presentation.components.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getSurahName(number: Int): String {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(
        "surah_name$number", "string", context.packageName
    )
    return if (resId != 0) context.getString(resId) else ""
}