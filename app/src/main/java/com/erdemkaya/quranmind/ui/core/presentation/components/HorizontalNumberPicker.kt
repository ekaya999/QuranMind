package com.erdemkaya.quranmind.ui.core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.erdemkaya.quranmind.ui.core.presentation.components.util.getSurahName
import kotlinx.coroutines.launch
import java.text.Normalizer
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalNumberPicker(
    modifier: Modifier = Modifier,
    minValue: Int = 1,
    maxValue: Int,
    selectedValue: Int,
    onValueSelected: (Int) -> Unit,
    isSurah: Boolean = false,
    showAlphabetical: Int = 0
) {
    val numbers = (minValue..maxValue).toList()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(maxValue) {
        coroutineScope.launch {
            listState.scrollToItem(0)
            onValueSelected(1)
        }
    }
    val context = LocalContext.current
    val alphabeticalSurahList = remember {
        (1..114).map { number ->
            val resId = context.resources.getIdentifier("surah_name$number", "string", context.packageName)
            val name = if (resId != 0) context.getString(resId) else ""
            number to name
        }.sortedBy { normalize(it.second) }
    }

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 100.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
    ) {
        val displayList = when (showAlphabetical) {
            0,2 -> numbers //
            1 -> alphabeticalSurahList.map { it.first } //
            else -> numbers
        }

        items(displayList.size) { index ->
            val number = displayList[index]
            val isSelected = number == selectedValue

            val scale by animateFloatAsState(targetValue = if (isSelected) 1.5f else 1f)
            val alpha by animateFloatAsState(targetValue = if (isSelected) 1f else 0.5f)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(60.dp)
                    .scale(scale)
                    .alpha(alpha)
                    .clickable {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index)
                        }
                        onValueSelected(number)
                    }) {
                Text(
                    text = when {
                        !isSurah -> number.toString()
                        showAlphabetical == 2 -> number.toString()
                        else -> {
                            getSurahName(number)
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.background else Color.Gray

                )
            }
        }
    }
}

fun normalize(text: String): String {
    return Normalizer
        .normalize(text, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .lowercase(Locale.getDefault())
}