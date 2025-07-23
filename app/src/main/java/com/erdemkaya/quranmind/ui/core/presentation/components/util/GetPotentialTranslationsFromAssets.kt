package com.erdemkaya.quranmind.ui.core.presentation.components.util

import android.content.Context

fun getPotentialTranslationsFromAssets(context: Context): List<PotentialTranslation> {
    val xmlFiles = context.assets.list("")?.filter { it.endsWith(".xml") && !it.contains("arabic") } ?: emptyList()
    return xmlFiles.map { file ->
        val langCode = when {
            file.startsWith("tr_") -> "tr"
            file.startsWith("sq_") -> "sq"
            file.startsWith("az_") -> "az"
            file.startsWith("bn_") -> "bn"
            file.startsWith("bs_") -> "bs"
            file.startsWith("bg_") -> "bg"
            file.startsWith("zh_") -> "zh"
            file.startsWith("de") -> "de"
            file.contains("arabic", ignoreCase = true) -> "ar"
            else -> ""
        }
        val translator = getTranslatorFromFile(file)
        PotentialTranslation(langCode, translator, file)
    }
}

data class PotentialTranslation(val lang: String, val translator: String, val fileName: String)