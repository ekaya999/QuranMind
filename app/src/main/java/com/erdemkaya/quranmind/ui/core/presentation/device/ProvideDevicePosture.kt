package com.erdemkaya.quranmind.ui.core.presentation.device

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun ProvideDevicePosture(
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    val posture = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> DevicePosture.Landscape
        else -> {
            if (screenWidthDp >= 600) {
                DevicePosture.Tablet
            } else {
                DevicePosture.Portrait
            }
        }
    }

    CompositionLocalProvider(LocalDevicePosture provides posture) {
        content()
    }
}