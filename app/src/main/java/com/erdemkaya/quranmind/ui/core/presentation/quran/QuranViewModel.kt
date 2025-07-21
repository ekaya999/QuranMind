package com.erdemkaya.quranmind.ui.core.presentation.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erdemkaya.quranmind.ui.core.data.di.quran.QuranRepositoryImpl
import com.erdemkaya.quranmind.ui.core.database.DatabaseInitializer
import com.erdemkaya.quranmind.ui.core.presentation.quran.mapper.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuranViewModel(
    private val repository: QuranRepositoryImpl, private val initializer: DatabaseInitializer
) : ViewModel() {

    private val _state = MutableStateFlow(QuranState())
    val state: StateFlow<QuranState> = _state.asStateFlow()

    private val _isInitializing = MutableStateFlow(true)
    val isInitializing = _isInitializing.asStateFlow()

    init {
        viewModelScope.launch {
            initializer.initializeDatabase {
                _isInitializing.value = false
                loadAvailableTranslations()
            }
        }
    }

    fun onAction(action: QuranAction) {
        when (action) {
            is QuranAction.OnSelectTranslationClick -> Unit
            is QuranAction.OnToggleTranslation -> toggleTranslation(
                action.languageCode,
                action.translatorName
            )

            is QuranAction.OnSearchSurahClick -> loadSurah(action.surahNumber)
            is QuranAction.OnSearchSurahAndAyahClick -> {
                loadSurah(action.surahNumber)
                selectAyah(action.ayahNumber)
            }
            else -> Unit
        }
    }

    private fun selectAyah(ayahNumber: Int) {
        _state.update { it.copy(selectedAyahNumber = ayahNumber) }
    }

    fun loadSurah(surahNumber: Int, ayahNumber: Int? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getVersesWithTranslations(surahNumber).catch { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false, error = exception.message ?: "An error occurred"
                        )
                    }
                }.collect { versesWithTranslations ->
                    val verseUiModels = versesWithTranslations.map { verseWithTranslation ->
                        verseWithTranslation.toUiModel(_state.value.selectedTranslation)
                    }

                    _state.update {
                        it.copy(
                            verses = verseUiModels,
                            isLoading = false,
                            currentSurahNumber = surahNumber
                        )
                    }
                }
        }
    }

    fun toggleTranslation(languageCode: String, translatorName: String) {
        val translationKey = "${languageCode}_${translatorName}"

        _state.update {
            it.copy(
                selectedTranslation = translationKey
            )
        }

        // Reload current surah with updated translations
        _state.value.currentSurahNumber.let { loadSurah(it) }
    }

    fun toggleBookmark(verseId: Int) {
        // This would require adding bookmark functionality to the repository
        _state.update { currentState ->
            currentState.copy(
                verses = currentState.verses.map { verse ->
                    if (verse.id == verseId) {
                        verse.copy(isBookmarked = !verse.isBookmarked)
                    } else {
                        verse
                    }
                })
        }
    }

    fun searchVerses(query: String) {
        if (query.isBlank()) {
            _state.update { it.copy(searchQuery = "", searchResults = emptyList()) }
            return
        }

        _state.update { it.copy(searchQuery = query) }

        // Filter verses based on search query
        val searchResults = _state.value.verses.filter { verse ->
            verse.arabicText.contains(
                query,
                ignoreCase = true
            ) || verse.translations.any { translation ->
                translation.text.contains(query, ignoreCase = true)
            }
        }

        _state.update { it.copy(searchResults = searchResults) }
    }

    private fun loadAvailableTranslations() {
        viewModelScope.launch {
            try {
                val translations = repository.getAvailableTranslations()
                _state.update {
                    it.copy(
                        availableTranslations = translations,
                        selectedTranslations = if (translations.isNotEmpty()) {
                            setOf("${translations.first().languageCode}_${translations.first().translatorName}")
                        } else {
                            emptySet()
                        }
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Failed to load translations: ${e.message}")
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}