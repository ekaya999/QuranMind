// QuranScreen.kt
package com.erdemkaya.quranmind.ui.core.presentation.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindNavBar
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindScaffold
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindTopBar
import com.erdemkaya.quranmind.ui.core.presentation.quran.model.VerseUiModel

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun QuranScreenRoot(
    state: QuranState,
    viewModel: QuranViewModel,
    onHomeClick: () -> Unit,
    onDuaClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    QuranScreen(
        state = state, onAction = { action ->
            when (action) {
                is QuranAction.OnHomeClick -> onHomeClick()
                is QuranAction.OnDuaClick -> onDuaClick()
                is QuranAction.OnProfileClick -> onProfileClick()
                else -> Unit
            }
            viewModel.onAction(action)
        })
}

@Composable
fun QuranScreen(
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
            title = "${getSurahName(state.currentSurahNumber)} Sûresi",
            onSurahSelected = { surah, ayah ->
                onAction(QuranAction.OnSearchSurahAndAyahClick(surah, ayah))
            })
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        VersesList(
                            verses = state.verses,
                            selectedAyah = state.selectedAyahNumber,
                            onBookmarkClick = { })

                    }
                }
            }
        }
    })
}

@Composable
fun VersesList(
    verses: List<VerseUiModel>,
    onBookmarkClick: (Int) -> Unit,
    selectedAyah: Int?,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(selectedAyah) {
        selectedAyah?.let { ayahNumber ->
            val index = verses.indexOfFirst { it.verseNumber == ayahNumber.toString() }
            if (index >= 0) {
                listState.animateScrollToItem(index)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(verses) { verse ->
            VerseCard(
                verse = verse, onBookmarkClick = { onBookmarkClick(verse.id) })
        }
    }
}

@Composable
fun VerseCard(
    verse: VerseUiModel, onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Verse number and bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "<< ${verse.verseNumber} >>",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.background
                )

//                IconButton(onClick = onBookmarkClick) {
//                    Icon(
//                        imageVector = if (verse.isBookmarked) {
//                            androidx.compose.material.icons.Icons.Filled.Favorite
//                        } else {
//                            androidx.compose.material.icons.Icons.Outlined.FavoriteBorder
//                        },
//                        contentDescription = "Bookmark",
//                        tint = if (verse.isBookmarked) Color.Red else Color.Gray
//                    )
//                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Arabic text
            Text(
                text = verse.arabicText, style = MaterialTheme.typography.bodyLarge.copy(
                    textDirection = TextDirection.Rtl
                ), modifier = Modifier.fillMaxWidth()
            )

            // Translations
            verse.translations.forEach { translation ->
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = translation.translatorInfo,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = translation.text,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ErrorMessage(
    error: String, onRetry: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun getSurahName(number: Int): String {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(
        "surah_name$number", "string", context.packageName
    )
    return if (resId != 0) context.getString(resId) else ""
}