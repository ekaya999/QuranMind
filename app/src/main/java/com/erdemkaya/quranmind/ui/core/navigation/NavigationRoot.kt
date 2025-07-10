package com.erdemkaya.quranmind.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.erdemkaya.quranmind.ui.core.presentation.home.HomeScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController, startDestination = "home"
    ) {
        homeGraph(navController)
    }
}

private fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigation(
        startDestination = "homeScreen", route = "home"
    ) {
        composable(route = "homeScreen") {
            HomeScreenRoot()
        }
    }
}