package com.erdemkaya.quranmind.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.erdemkaya.quranmind.R

val Cormorant = FontFamily(
    Font(R.font.cormorant_garamond_regular, FontWeight.Normal),
    Font(R.font.cormorant_garamond_medium, FontWeight.Medium),
    Font(R.font.cormorant_garamond_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Bold, fontSize = 48.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Bold, fontSize = 40.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Bold, fontSize = 32.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Bold, fontSize = 28.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Bold, fontSize = 26.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Bold, fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Medium, fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Medium, fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Medium, fontSize = 17.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Medium, fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Normal, fontSize = 11.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Medium, fontSize = 15.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Cormorant, fontWeight = FontWeight.Normal, fontSize = 15.sp
    ),
)