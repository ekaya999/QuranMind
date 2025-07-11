package com.erdemkaya.quranmind.ui.core.presentation.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.erdemkaya.quranmind.ui.core.presentation.device.DevicePosture
import com.erdemkaya.quranmind.ui.core.presentation.device.LocalDevicePosture

@Composable
fun HomeScreenRoot(
    navHostController: NavHostController
) {
    val posture = LocalDevicePosture.current
    when (posture) {
        DevicePosture.Landscape -> HomeScreenLandscape(navHostController = navHostController)
        DevicePosture.Tablet -> HomeScreenTablet(navHostController = navHostController)
        DevicePosture.Portrait -> HomeScreenPortrait(navHostController = navHostController)
    }
}