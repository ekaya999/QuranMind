package com.erdemkaya.quranmind.ui.core.data.di.home

import android.content.Context
import android.graphics.Bitmap
import com.erdemkaya.quranmind.ui.core.domain.home.HomeRepository
import com.erdemkaya.quranmind.ui.core.presentation.components.saveToCacheAndGetUri
import com.erdemkaya.quranmind.ui.core.presentation.components.saveToGallery
import com.erdemkaya.quranmind.ui.core.presentation.components.shareImageUri

class HomeRepositoryImpl(
    private val context: Context
) : HomeRepository {
    override fun saveImageToGallery(bitmap: Bitmap): Boolean {
        return bitmap.saveToGallery(context)
    }

    override fun shareImage(bitmap: Bitmap) {
        val uri = bitmap.saveToCacheAndGetUri(context)
        shareImageUri(context, uri)
    }
}