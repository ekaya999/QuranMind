package com.erdemkaya.quranmind.ui.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QuranMindScaffold(
    modifier: Modifier = Modifier,
    topAppbar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = topAppbar, modifier = modifier, bottomBar = bottomBar
    ) { paddingValues ->
        content(paddingValues)
    }
}