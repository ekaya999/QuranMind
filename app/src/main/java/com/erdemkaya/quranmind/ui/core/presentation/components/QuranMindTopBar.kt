package com.erdemkaya.quranmind.ui.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.erdemkaya.quranmind.R
import com.erdemkaya.quranmind.ui.core.presentation.components.util.surahAyahCounts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranMindTopBar(
    modifier: Modifier = Modifier,
    title: String,
    showSearch: Boolean = false,
    onSurahSelected: (Int, Int) -> Unit,
    onTranslationSelectClick: (() -> Unit)? = null,
    selectedLanguage: String
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Column(
        modifier = modifier,
    ) {
        var showDialog by remember { mutableStateOf(false) }

        TopAppBar(
            scrollBehavior = scrollBehavior, title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ), actions = {
                if (onTranslationSelectClick != null) {
                    IconButton(onClick = onTranslationSelectClick) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_language_24),
                            contentDescription = "Select Translation",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                if (showSearch) {
                    IconButton(
                        onClick = { showDialog = true },
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            })
        HorizontalDivider(
            thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground
        )
        if (showDialog) {
            SurahAyahDialog(onDismiss = { showDialog = false }, onConfirm = { surah, ayah ->
                showDialog = false
                onSurahSelected(surah, ayah)
            },
                selectedLanguage)
        }
    }
}

@Composable
fun SurahAyahDialog(
    onDismiss: () -> Unit, onConfirm: (Int, Int) -> Unit,
    selectedLanguage: String
) {
    var selectedSurah by remember { mutableIntStateOf(1) }
    var selectedAyah by remember { mutableIntStateOf(1) }
    var showAlphabetical by remember { mutableIntStateOf(0) }
    val maxAyah = surahAyahCounts.getOrNull(selectedSurah - 1) ?: 286

    AlertDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = { onConfirm(selectedSurah, selectedAyah) }) {
            Text(
                "Git",
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(
                "İptal",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }, title = {
        Text(
            "Sûre ve Ayet seç", style = MaterialTheme.typography.titleLarge
        )
    }, text = {
        Column {
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Sûre: $selectedSurah")
                Text(
                    text = if (showAlphabetical == 0) "A .. Z" else if (showAlphabetical == 1) "1 .. 114" else "1..114 (Sûre ismi)",
                    modifier = Modifier.clickable {
                        if (showAlphabetical == 0) showAlphabetical =
                            1 else if (showAlphabetical == 1) showAlphabetical =
                            2 else showAlphabetical = 0
                    })

            }
            Spacer(Modifier.height(24.dp))
            HorizontalNumberPicker(
                minValue = 1,
                maxValue = 114,
                selectedValue = selectedSurah,
                onValueSelected = { selectedSurah = it },
                isSurah = true,
                showAlphabetical = showAlphabetical,
                selectedLanguage = selectedLanguage
            )
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))
            Text("Ayet", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
            Spacer(Modifier.height(24.dp))
            HorizontalNumberPicker(
                minValue = 1,
                maxValue = maxAyah,
                selectedValue = selectedAyah,
                onValueSelected = { selectedAyah = it },
                isSurah = false,
                selectedLanguage = selectedLanguage
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    })
}

//@Preview
//@Composable
//private fun QuranMindTopBarPreview() {
//    QuranMindTheme {
//        QuranMindTopBar(
//            title = "Kur'an-ı Kerim",
//            onSurahSelected = {}
//        )
//    }
//}