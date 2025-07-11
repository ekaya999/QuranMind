package com.erdemkaya.quranmind.ui.core.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.erdemkaya.quranmind.R
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindNavBar
import com.erdemkaya.quranmind.ui.core.presentation.components.QuranMindScaffold
import com.erdemkaya.quranmind.ui.core.presentation.components.ShareableCard
import com.erdemkaya.quranmind.ui.core.presentation.components.saveToCacheAndGetUri
import com.erdemkaya.quranmind.ui.core.presentation.components.saveToGallery
import com.erdemkaya.quranmind.ui.core.presentation.components.shareImageUri
import com.erdemkaya.quranmind.ui.core.presentation.components.util.hijriDateText
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreenTablet(
    navHostController: NavHostController
) {

    val context = LocalContext.current
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val textArabic = "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا"
    val textTurkish = "Şüphesiz, zorlukla beraber bir kolaylık vardır."
    val quranSure = "İnşirah Suresi, 94:6"

    QuranMindScaffold(bottomBar = {
        QuranMindNavBar(
            currentScreen = "home", navHostController = navHostController, modifier = Modifier
        )
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
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

            Box {
                ShareableCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .aspectRatio(1.5f),
                    textArabic = textArabic,
                    textTurkish = textTurkish,
                    quranSure = quranSure,
                    onBitmapCaptured = { },
                    showBorder = true,
                    showSignature = false
                )

                ShareableCard(
                    modifier = Modifier
                        .width(500.dp)
                        .padding(horizontal = 16.dp)
                        .aspectRatio(1f)
                        .alpha(0f),
                    textArabic = textArabic,
                    textTurkish = textTurkish,
                    quranSure = quranSure,
                    onBitmapCaptured = {
                        imageBitmap.value = it
                    },
                    showBorder = false,
                    showSignature = true
                )
            }

            Spacer(Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {

                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_favorite_24),
                        contentDescription = "Like",
                        modifier = Modifier.clip(
                            RoundedCornerShape(100)
                        ),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(
                    onClick = {
                        imageBitmap.value?.let { bitmap ->
                            val androidBitmap = bitmap.asAndroidBitmap()

                            // In Galerie speichern
                            val saved = androidBitmap.saveToGallery(context)
                            if (saved) {
                                Toast.makeText(
                                    context, "Görsel galeriye kaydedildi", Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context, "Görsel kaydedilemedi", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_save_alt_24),
                        contentDescription = "Save",
                        modifier = Modifier.clip(
                            RoundedCornerShape(100)
                        ),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(
                    onClick = {
                        imageBitmap.value?.let {
                            val bmp = it.asAndroidBitmap()
                            val uri = bmp.saveToCacheAndGetUri(context)
                            shareImageUri(context, uri)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "share",
                        modifier = Modifier.clip(
                            RoundedCornerShape(100)
                        ),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    })
}



//@Preview(showBackground = true, backgroundColor = 0xFF0C3B2E)
//@Composable
//private fun HomeScreenPortraitPreview() {
//    QuranMindTheme {
//        HomeScreenPortrait()
//    }
//}