package com.erdemkaya.quranmind.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.erdemkaya.quranmind.ui.core.presentation.home.HomeScreenRoot
import com.erdemkaya.quranmind.ui.core.presentation.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoot(
    navController: NavHostController,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val homeState by homeViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController, startDestination = "home"
    ) {
        composable(route = "home") {
            HomeScreenRoot(
                onDuaClick = { navController.navigate("dua") },
                onQuranClick = { navController.navigate("quran") },
                onProfileClick = { navController.navigate("profile") },
                viewModel = homeViewModel,
                state = homeState
            )
        }
    }
}
