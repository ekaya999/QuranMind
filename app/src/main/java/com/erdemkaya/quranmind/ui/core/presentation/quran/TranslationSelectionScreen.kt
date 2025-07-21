package com.erdemkaya.quranmind.ui.core.presentation.quran

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erdemkaya.quranmind.ui.core.database.dao.quran.TranslationInfo
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindNavBar
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindScaffold
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindTopBar
import com.erdemkaya.quranmind.ui.core.presentation.components.util.getLanguageName
import com.erdemkaya.quranmind.ui.theme.QuranMindTheme

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
            state = state, onAction = { action ->
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
    state: QuranState,
    onAction: (QuranAction) -> Unit,
) {
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
            title = "Kur'an-ı Kerim", onSurahSelected = { _, _ -> }, showSearch = false
        )
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val grouped = state.availableTranslations.groupBy { it.languageCode }

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
                    grouped.forEach { (languageCode, translations) ->
                        item {
                            Text(
                                text = getLanguageName(languageCode),
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
                        items(translations) { translation ->
                            TranslationOption(
                                translation = translation,
                                isSelected = "${translation.languageCode}_${translation.translatorName}" in state.selectedTranslation,
                                onToggle = {
                                    onAction(
                                        QuranAction.OnToggleTranslation(
                                            languageCode = translation.languageCode,
                                            translatorName = translation.translatorName
                                        )
                                    )
                                })
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.selectedTranslation.isNotEmpty()) {
                Button(
                    onClick = { onAction(QuranAction.OnSelectTranslationClick) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Devam Et",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Çeviri seçin",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    })
}


@Composable
fun TranslationOption(
    translation: TranslationInfo, isSelected: Boolean, onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected, onClick = onToggle
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected, onClick = onToggle, colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.background,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Text(
            text = translation.translatorName,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C3B2E)
@Composable
private fun TranslationSelectionScreenPreview() {
    QuranMindTheme {
        TranslationSelectionScreen(
            state = QuranState(), onAction = {})
    }

}