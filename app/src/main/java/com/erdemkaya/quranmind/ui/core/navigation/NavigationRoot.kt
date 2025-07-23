package com.erdemkaya.quranmind.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.erdemkaya.quranmind.ui.core.presentation.home.HomeScreenRoot
import com.erdemkaya.quranmind.ui.core.presentation.home.HomeViewModel
import com.erdemkaya.quranmind.ui.core.presentation.quran.QuranAction
import com.erdemkaya.quranmind.ui.core.presentation.quran.QuranScreenRoot
import com.erdemkaya.quranmind.ui.core.presentation.quran.QuranViewModel
import com.erdemkaya.quranmind.ui.core.presentation.quran.TranslationSelectionScreenRoot
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoot(
    navController: NavHostController,
    homeViewModel: HomeViewModel = koinViewModel(),
    quranViewModel: QuranViewModel = koinViewModel()
) {
    val homeState by homeViewModel.state.collectAsStateWithLifecycle()
    val quranState by quranViewModel.state.collectAsStateWithLifecycle()
    val onRemoveTranslation = { quranViewModel.onAction(QuranAction.OnRemoveTranslation) }

    NavHost(
        navController = navController, startDestination = "home"
    ) {
        composable(route = "home") {
            HomeScreenRoot(
                onDuaClick = { navController.navigate("dua") },
                onQuranClick = { navController.navigate("translation") },
                onProfileClick = { navController.navigate("profile") },
                viewModel = homeViewModel,
                state = homeState
            )
        }
        composable(route = "translation") {
            if (quranState.selectedTranslation.isNotEmpty()) {
                // Redirect to Quran screen if a translation is already selected
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    navController.navigate("quran") {
                        popUpTo("translation") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            } else {
                TranslationSelectionScreenRoot(
                    onTranslationSelected = { navController.navigate("quran") },
                    state = quranState,
                    viewModel = quranViewModel,
                    onHomeClick = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onDuaClick = { navController.navigate("dua") },
                    onProfileClick = { navController.navigate("profile") })
            }
        }

        composable("quran") {
            QuranScreenRoot(
                state = quranState,
                viewModel = quranViewModel,
                onHomeClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onDuaClick = { navController.navigate("dua") },
                onProfileClick = { navController.navigate("profile") },
                onTranslationSelectClick = {
                    onRemoveTranslation()
                    navController.navigate("translation") }
            )
        }
    }
}
