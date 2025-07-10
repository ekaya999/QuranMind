package com.erdemkaya.quranmind.ui.core.presentation.home

import androidx.compose.runtime.Composable
import com.erdemkaya.quranmind.ui.core.presentation.device.DevicePosture
import com.erdemkaya.quranmind.ui.core.presentation.device.LocalDevicePosture

@Composable
fun HomeScreenRoot() {
    val posture = LocalDevicePosture.current
    when (posture) {
        //DevicePosture.Landscape -> HomeScreenLandscape
        //DevicePosture.Tablet -> HomeScreenTablet
        DevicePosture.Portrait -> HomeScreenPortrait()
        else -> null
    }
}