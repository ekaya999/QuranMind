package com.erdemkaya.quranmind.ui.core.presentation.components.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import com.erdemkaya.quranmind.R
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.util.Calendar

@Composable
fun hijriDateText(): String {
    val context = LocalContext.current

    val hijri = remember {
        val uCal = UmmalquraCalendar()
        val day = uCal.get(Calendar.DAY_OF_MONTH)
        val month = uCal.get(Calendar.MONTH) + 1
        val year = uCal.get(Calendar.YEAR)
        val months = context.resources.getStringArray(R.array.hijri_months)
        "$day ${months[month - 1]} $year"
    }

    return hijri
}