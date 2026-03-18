package com.example.flippers.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flippers.ui.screen.HomeScreen
import com.example.flippers.ui.screen.SplashFeaturesScreen
import com.example.flippers.ui.screen.SplashReadyScreen
import com.example.flippers.ui.screen.SplashScreen
import com.example.flippers.ui.screen.scanner.BookScannerScreen
import com.example.flippers.ui.screen.scanner.DocumentScannerScreen
import com.example.flippers.ui.screen.scanner.IdCardScannerScreen
import com.example.flippers.ui.screen.scanner.QrCodeScannerScreen
import com.example.flippers.ui.screen.scanner.ScanResultScreen

@Composable
fun FlippersNavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(
                onNext = {
                    navController.navigate(Screen.SplashFeatures.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SplashFeatures.route) {
            SplashFeaturesScreen(
                onNext = {
                    navController.navigate(Screen.SplashReady.route) {
                        popUpTo(Screen.SplashFeatures.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SplashReady.route) {
            SplashReadyScreen(
                onGetStarted = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SplashReady.route) { inclusive = true }
                    }
                }
            )
        }

        // Auth screens commented out — reserved for Pro plan
        // composable(Screen.Onboarding.route) { ... }
        // composable(Screen.LetYouIn.route) { ... }
        // composable(Screen.SignUp.route) { ... }
        // composable(Screen.SignUpSuccess.route) { ... }
        // composable(Screen.SignIn.route) { ... }
        // composable(Screen.ForgotPassword.route) { ... }
        // composable(Screen.OtpVerification.route) { ... }
        // composable(Screen.CreateNewPassword.route) { ... }
        // composable(Screen.ResetPasswordSuccess.route) { ... }
        // composable(Screen.ProfileSetup.route) { ... }

        composable(Screen.Home.route) {
            HomeScreen(
                onScanClick = { navController.navigate(Screen.DocumentScanner.route) },
                onQrScanClick = { navController.navigate(Screen.QrCodeScanner.route) }
            )
        }

        composable(Screen.QrCodeScanner.route) {
            QrCodeScannerScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.DocumentScanner.route) {
            DocumentScannerScreen(
                onNavigateBack = { navController.popBackStack() },
                onImageCaptured = { path ->
                    navController.navigate(Screen.ScanResult.createRoute(path))
                }
            )
        }

        composable(Screen.BookScanner.route) {
            BookScannerScreen(
                onNavigateBack = { navController.popBackStack() },
                onImageCaptured = { path ->
                    navController.navigate(Screen.ScanResult.createRoute(path))
                }
            )
        }

        composable(Screen.IdCardScanner.route) {
            IdCardScannerScreen(
                onNavigateBack = { navController.popBackStack() },
                onImageCaptured = { path ->
                    navController.navigate(Screen.ScanResult.createRoute(path))
                }
            )
        }

        composable(
            route = Screen.ScanResult.route,
            arguments = listOf(navArgument("imagePath") { type = NavType.StringType })
        ) { backStackEntry ->
            val imagePath = backStackEntry.arguments?.getString("imagePath") ?: ""
            ScanResultScreen(
                imagePath = imagePath,
                onNavigateBack = { navController.popBackStack() },
                onRetake = {
                    navController.popBackStack()
                },
                onSave = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
