package com.example.flippers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.flippers.navigation.FlippersNavGraph
import com.example.flippers.ui.theme.FlippersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlippersTheme {
                val navController = rememberNavController()
                FlippersNavGraph(navController = navController)
            }
        }
    }
}
