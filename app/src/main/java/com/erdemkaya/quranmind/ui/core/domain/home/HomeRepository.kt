package com.erdemkaya.quranmind.ui.core.domain.home

import android.graphics.Bitmap

interface HomeRepository {
    fun saveImageToGallery(bitmap: Bitmap): Boolean
    fun shareImage(bitmap: Bitmap)
}