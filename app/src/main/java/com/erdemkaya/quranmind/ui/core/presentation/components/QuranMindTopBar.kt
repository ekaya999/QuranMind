package com.erdemkaya.quranmind.ui.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranMindTopBar(
    modifier: Modifier = Modifier, title: String, onSurahSelected: (Int, Int) -> Unit
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
                IconButton(
                    onClick = { showDialog = true },
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            })
        HorizontalDivider(
            thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground
        )
        if (showDialog) {
            SurahAyahDialog(onDismiss = { showDialog = false }, onConfirm = { surah, ayah ->
                showDialog = false
                onSurahSelected(surah, ayah)
            })
        }
    }
}

@Composable
fun SurahAyahDialog(
    onDismiss: () -> Unit, onConfirm: (Int, Int) -> Unit
) {
    var selectedSurah by remember { mutableIntStateOf(1) }
    var selectedAyah by remember { mutableIntStateOf(1) }

    AlertDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = { onConfirm(selectedSurah, selectedAyah) }) {
            Text("Git")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("İptal")
        }
    }, title = { Text("Sure Seç") }, text = {
        Column {
            Text("Sure:")
            Slider(
                value = selectedSurah.toFloat(),
                onValueChange = { selectedSurah = it.toInt() },
                valueRange = 1f..114f,
                steps = 112
            )
            Text("Seçilen Sure: $selectedSurah")

            Text("Ayet:")
            Slider(
                value = selectedAyah.toFloat(),
                onValueChange = { selectedAyah = it.toInt() },
                valueRange = 1f..268f,
                steps = 266
            )
            Text("Seçilen Ayet: $selectedAyah")

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