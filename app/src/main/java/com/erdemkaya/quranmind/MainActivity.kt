package com.erdemkaya.quranmind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.erdemkaya.quranmind.ui.core.navigation.NavigationRoot
import com.erdemkaya.quranmind.ui.core.presentation.device.ProvideDevicePosture
import com.erdemkaya.quranmind.ui.theme.QuranMindTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuranMindTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ProvideDevicePosture {
                        val navHostController = rememberNavController()
                        NavigationRoot(
                            navController = navHostController
                        )
                    }
                }
            }
        }
    }
}