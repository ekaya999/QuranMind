package com.erdemkaya.quranmind.ui.core.presentation.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.erdemkaya.quranmind.ui.core.presentation.device.DevicePosture
import com.erdemkaya.quranmind.ui.core.presentation.device.LocalDevicePosture
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoot(
    onQuranClick: () -> Unit,
    onDuaClick: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    state: HomeState,
) {

    val onAction: (HomeAction) -> Unit = { action ->
        when (action) {
            HomeAction.OnQuranClick -> onQuranClick()
            HomeAction.OnDuaClick -> onDuaClick()
            HomeAction.OnProfileClick -> onProfileClick()
            else -> Unit
        }
        viewModel.onAction(action)
    }

    val posture = LocalDevicePosture.current
    when (posture) {
        DevicePosture.Landscape -> HomeScreenLandscape(state, onAction)
        DevicePosture.Tablet -> HomeScreenTablet(state, onAction)
        DevicePosture.Portrait -> HomeScreenPortrait(state, onAction)
    }
}