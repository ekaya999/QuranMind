package com.erdemkaya.quranmind.ui.core.presentation.components

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import com.erdemkaya.quranmind.R
import java.io.File
import java.io.FileOutputStream

@Composable
fun ShareableCard(
    modifier: Modifier = Modifier,
    borderColor: Color = Color(0xFFD7CDAF),
    backgroundColor: Color = Color(0xFF0C3B2E),
    cornerRadius: Dp = 12.dp,
    textArabic: String,
    textTurkish: String,
    quranSure: String,
    onBitmapCaptured: (ImageBitmap) -> Unit,
    showBorder: Boolean = true,
    showSignature: Boolean = false
) {
    val context = LocalContext.current
    val compositionContext = rememberCompositionContext()

    val container = remember {
        FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    val shape = RoundedCornerShape(cornerRadius)
    val surfaceModifier = if (showBorder) {
        Modifier
            .clip(shape)
            .border(1.dp, borderColor, shape)
    } else {
        Modifier
    }

    AndroidView(factory = {
        container
    }, update = { frameLayout ->
        frameLayout.removeAllViews()
        val composeView = ComposeView(context).apply {
            setParentCompositionContext(compositionContext)
            setContent {
                Surface(
                    color = backgroundColor,
                    modifier = surfaceModifier
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp, horizontal = 32.dp)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = textArabic,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontFamily = FontFamily(Font(R.font.amiri_bold))
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = textTurkish,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = quranSure,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.Center
                            )
                        }
                        if (showSignature) {
                            Text(
                                text = "by QuranMind",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                            )
                        }
                    }
                }
            }
        }
        frameLayout.addView(composeView)

        // Verwende die moderne Methode zum Erstellen eines Bitmaps
        composeView.post {
            // Warte bis das Layout vollständig ist
            composeView.doOnLayout {
                val bitmap = createBitmapFromView(it)
                bitmap?.let { bmp ->
                    onBitmapCaptured(bmp.asImageBitmap())
                }
            }
        }
    }, modifier = modifier)
}

// Extension function um auf das Layout zu warten
private fun View.doOnLayout(action: (view: View) -> Unit) {
    if (isLaidOut && !isLayoutRequested) {
        action(this)
    } else {
        viewTreeObserver.addOnGlobalLayoutListener(object : android.view.ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                action(this@doOnLayout)
            }
        })
    }
}

// Moderne Methode zum Erstellen eines Bitmaps von einer View
private fun createBitmapFromView(view: View): Bitmap? {
    return try {
        val width = view.width
        val height = view.height

        if (width <= 0 || height <= 0) {
            return null
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Bitmap.saveToCacheAndGetUri(context: Context): Uri {
    val file = File(context.cacheDir, "shared_card.png")
    FileOutputStream(file).use { out -> compress(Bitmap.CompressFormat.PNG, 100, out) }
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

fun shareImageUri(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val chooserIntent = Intent.createChooser(intent, "Görseli paylaş").apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    context.startActivity(chooserIntent)
}

fun Bitmap.saveToGallery(context: Context): Boolean {
    return try {
        val filename = "QuranMind_${System.currentTimeMillis()}.png"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // Android 10 und höher - MediaStore verwenden
            val resolver = context.contentResolver
            val contentValues = android.content.ContentValues().apply {
                put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_PICTURES + "/QuranMind")
            }

            val uri = resolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
            }
        } else {
            // Android 9 und niedriger - direkter Dateizugriff
            val imagesDir = android.os.Environment.getExternalStoragePublicDirectory(
                android.os.Environment.DIRECTORY_PICTURES
            )
            val appDir = File(imagesDir, "QuranMind")
            if (!appDir.exists()) {
                appDir.mkdirs()
            }

            val file = File(appDir, filename)
            FileOutputStream(file).use { outputStream ->
                this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }

            // Media Scanner benachrichtigen
            android.media.MediaScannerConnection.scanFile(
                context,
                arrayOf(file.absolutePath),
                arrayOf("image/png"),
                null
            )
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}