package com.erdemkaya.quranmind.ui.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.erdemkaya.quranmind.ui.core.presentation.components.util.QuranMindNavBarItem

@Composable
fun QuranMindNavBar(
    currentScreen: String, navHostController: NavHostController, modifier: Modifier
) {
    val items = listOf(
        QuranMindNavBarItem.Home, QuranMindNavBarItem.Quran, QuranMindNavBarItem.Dua,
        QuranMindNavBarItem.Profile
    )

    Column(modifier = modifier) {
        HorizontalDivider(
            thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground
        )

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentScreen == item.screenRoute,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedIconColor = Color.White,
                        indicatorColor = Color.Transparent
                    ),
                    onClick = {
                        navHostController.navigate(item.screenRoute)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = item.screenRoute
                        )
                    },
                    label = {
                        Text(
                            text = item.title, style = MaterialTheme.typography.labelMedium,
                            color = if (currentScreen == item.screenRoute) MaterialTheme.colorScheme.onBackground else Color.White
                        )
                    })
            }
        }
    }
}