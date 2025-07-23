package com.erdemkaya.quranmind.ui.core.presentation.quran

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erdemkaya.quranmind.R
import com.erdemkaya.quranmind.ui.core.database.dao.quran.TranslationInfo
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindNavBar
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindScaffold
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindTopBar
import com.erdemkaya.quranmind.ui.core.presentation.components.util.PotentialTranslation
import com.erdemkaya.quranmind.ui.core.presentation.components.util.getLanguageName
import com.erdemkaya.quranmind.ui.core.presentation.components.util.getPotentialTranslationsFromAssets
import com.erdemkaya.quranmind.ui.core.presentation.components.util.normalizeForSorting
import com.erdemkaya.quranmind.ui.theme.QuranMindTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun TranslationSelectionScreenRoot(
    state: QuranState,
    onTranslationSelected: () -> Unit,
    viewModel: QuranViewModel,
    onHomeClick: () -> Unit,
    onDuaClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val isInitializing by viewModel.isInitializing.collectAsState()
    if (isInitializing) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        TranslationSelectionScreen(
            state = state, viewModel = viewModel, onAction = { action ->
                when (action) {
                    is QuranAction.OnSelectTranslationClick -> onTranslationSelected()
                    is QuranAction.OnHomeClick -> onHomeClick()
                    is QuranAction.OnDuaClick -> onDuaClick()
                    is QuranAction.OnProfileClick -> onProfileClick()
                    else -> Unit
                }
                viewModel.onAction(action)
            })
    }
}

@Composable
fun TranslationSelectionScreen(
    state: QuranState, onAction: (QuranAction) -> Unit, viewModel: QuranViewModel
) {
    val context = LocalContext.current
    val potentialTranslations = remember { getPotentialTranslationsFromAssets(context) }
    val groupedPotential = potentialTranslations.groupBy { it.lang }
    val groupedLoaded = state.availableTranslations.groupBy { it.languageCode }
    val deviceLanguage = LocalConfiguration.current.locales[0].language
    val allLanguageCodes = (groupedPotential.keys + groupedLoaded.keys).distinct()
    val sortedLanguageCodes = remember(allLanguageCodes, groupedLoaded) {
        allLanguageCodes.sortedWith(compareByDescending<String> { it == deviceLanguage }.thenByDescending { groupedLoaded[it]?.isNotEmpty() == true }
            .thenBy { getLanguageName(context, it).normalizeForSorting() })
    }

    QuranMindScaffold(bottomBar = {
        QuranMindNavBar(
            currentScreen = "quran", onNavItemClick = { route ->
                when (route) {
                    "home" -> onAction(QuranAction.OnHomeClick)
                    "quran" -> {}
                    "dua" -> onAction(QuranAction.OnDuaClick)
                    "profile" -> onAction(QuranAction.OnProfileClick)
                }
            }, modifier = Modifier
        )
    }, topAppbar = {
        QuranMindTopBar(
            title = "Kur'an-ı Kerim",
            onSurahSelected = { _, _ -> },
            showSearch = false,
            selectedLanguage = ""
        )
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sortedLanguageCodes.forEach { languageCode ->
                        item {
                            Text(
                                text = getLanguageName(context, languageCode),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(top = 4.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        val loadedTrans = groupedLoaded[languageCode]?.map {
                            PotentialTranslation(it.languageCode, it.translatorName, "")
                        } ?: emptyList()
                        val potentialTrans = groupedPotential[languageCode] ?: emptyList()
                        val allInGroup = (loadedTrans + potentialTrans).distinctBy { it.translator }

                        items(allInGroup, key = { "${it.lang}_${it.translator}" }) { trans ->
                            val uniqueKey = "${trans.lang}_${trans.translator}"
                            var isLoaded by remember(uniqueKey, state.availableTranslations.size) {
                                mutableStateOf<Boolean?>(null)
                            }

                            LaunchedEffect(
                                key1 = uniqueKey, key2 = state.availableTranslations.size
                            ) {
                                isLoaded = if (trans.fileName.isEmpty()) true
                                else viewModel.isTranslationLoaded(trans.lang, trans.translator)
                            }

                            when (isLoaded) {
                                null -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Prüfe Übersetzung...")
                                    }
                                }

                                true -> {
                                    TranslationOption(
                                        translation = TranslationInfo(trans.lang, trans.translator),
                                        isSelected = "${trans.lang}_${trans.translator}" == state.selectedTranslation,
                                        onToggle = {
                                            onAction(
                                                QuranAction.OnToggleTranslation(
                                                    trans.lang, trans.translator
                                                )
                                            )
                                        },
                                        loaded = true,
                                        onAddToTranslation = {})
                                }

                                false -> {
                                    TranslationOption(
                                        translation = TranslationInfo(trans.lang, trans.translator),
                                        isSelected = "${trans.lang}_${trans.translator}" == state.selectedTranslation,
                                        onToggle = {
                                            onAction(
                                                QuranAction.OnToggleTranslation(
                                                    trans.lang, trans.translator
                                                )
                                            )
                                        },
                                        loaded = false,
                                        onAddToTranslation = {
                                            onAction(
                                                QuranAction.OnAddTranslation(
                                                    trans.lang, trans.translator, trans.fileName
                                                )
                                            )
                                        })
                                }
                            }
                        }
                    }
                }
            }
        }
    })
}

@Composable
fun TranslationOption(
    translation: TranslationInfo,
    isSelected: Boolean,
    onToggle: () -> Unit,
    onAddToTranslation: () -> Unit,
    loaded: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        if (loaded) {
            RadioButton(
                selected = isSelected, onClick = onToggle, colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.background,
                    unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        } else {
            IconButton(onClick = onAddToTranslation) {
                Icon(
                    painter = painterResource(R.drawable.outline_save_alt_24),
                    contentDescription = "download translation",
                )
            }
        }
        Text(
            text = translation.translatorName,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                if (!loaded) {
                    onAddToTranslation()
                } else onToggle()
            })
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C3B2E)
@Composable
private fun TranslationSelectionScreenPreview() {
    QuranMindTheme {
        TranslationSelectionScreen(
            state = QuranState(), viewModel = koinViewModel(), onAction = {})
    }
}