package com.erdemkaya.quranmind.ui.core.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.erdemkaya.quranmind.ui.core.presentation.components.OutlinedGoldCard
import com.erdemkaya.quranmind.ui.theme.QuranMindTheme
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreenPortrait() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Selamün Aleyküm",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            "Erdem",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(24.dp))

        val locale = Locale("tr", "TR")
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", locale)
        val date = remember { LocalDate.now().format(formatter) }

        Text(
            text = "Bugün: $date",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Hicri: ${hijriDateText()}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(24.dp))

        OutlinedGoldCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).aspectRatio(1f),
            textArabic = "إِنَّ اللَّهَ لَا يَسْتَحْيِي", textTurkish = "Allah, hiç kimseye kapasitesinin üstünde bir yük yüklemez")
    }
}

@Composable
fun hijriDateText(): String {
    val hijri = remember {
        val uCal = UmmalquraCalendar()
        val day = uCal.get(Calendar.DAY_OF_MONTH)
        val month = uCal.get(Calendar.MONTH) + 1
        val year = uCal.get(Calendar.YEAR)
        val months = arrayOf(
            "Muharrem", "Safer", "Rebiülevvel", "Rebiülahir",
            "Cemaziyelevvel", "Cemaziyelahir", "Recep", "Şaban",
            "Ramazan", "Şevval", "Zilkade", "Zilhicce"
        )
        "$day ${months[month - 1]} $year"
    }

    return hijri
}

@Preview(showBackground = true, backgroundColor = 0xFF0C3B2E)
@Composable
private fun HomeScreenPortraitPreview() {
    QuranMindTheme {
        HomeScreenPortrait()
    }
}