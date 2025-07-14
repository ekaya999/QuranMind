package com.erdemkaya.quranmind.ui.core.presentation.home

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erdemkaya.quranmind.ui.core.domain.home.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.onStart {
        //
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), HomeState()
    )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnBitmapCreated -> onBitmapCreated(action.bitmap)
            HomeAction.OnSaveClick -> onSaveClick()
            HomeAction.OnShareClick -> onShareClick()
            else -> Unit
        }
    }

    private fun onShareClick() {
        _state.value.imageBitmap?.let { bitmap ->
            val bitmap = bitmap.asAndroidBitmap()
            homeRepository.shareImage(bitmap)
        }
    }

    private fun onSaveClick() {
        _state.value.imageBitmap?.let { bitmap ->
            val bitmap = bitmap.asAndroidBitmap()
            homeRepository.saveImageToGallery(bitmap)
        }
    }

    private fun onBitmapCreated(bitmap: ImageBitmap) {
        _state.update {
            it.copy(
                imageBitmap = bitmap
            )
        }
    }
}