package com.erdemkaya.quranmind.ui.core.presentation.components.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.util.Calendar

@Composable
fun hijriDateText(): String {
    val hijri = remember {
        val uCal = UmmalquraCalendar()
        val day = uCal.get(Calendar.DAY_OF_MONTH)
        val month = uCal.get(Calendar.MONTH) + 1
        val year = uCal.get(Calendar.YEAR)
        val months = arrayOf(
            "Muharrem",
            "Safer",
            "Rebiülevvel",
            "Rebiülahir",
            "Cemaziyelevvel",
            "Cemaziyelahir",
            "Recep",
            "Şaban",
            "Ramazan",
            "Şevval",
            "Zilkade",
            "Zilhicce"
        )
        "$day ${months[month - 1]} $year"
    }

    return hijri
}