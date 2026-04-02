package com.example.flippers.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.flippers.ui.components.BottomNavBar
import com.example.flippers.ui.components.bottomNavItems
import com.example.flippers.ui.components.bottomNavRoutes
import com.example.flippers.ui.screen.HomeScreen
import com.example.flippers.ui.screen.ProfileScreen
import com.example.flippers.ui.screen.SplashFeaturesScreen
import com.example.flippers.ui.screen.SplashReadyScreen
import com.example.flippers.ui.screen.SplashScreen
import com.example.flippers.ui.screen.analytics.AnalyticsDashboardScreen
import com.example.flippers.ui.screen.analytics.ScanStatsScreen
import com.example.flippers.ui.screen.enterprise.AdminDashboardScreen
import com.example.flippers.ui.screen.enterprise.AuditLogScreen
import com.example.flippers.ui.screen.enterprise.SecuritySettingsScreen
import com.example.flippers.ui.screen.enterprise.TeamManagementScreen
import com.example.flippers.ui.screen.integrations.IntegrationDetailScreen
import com.example.flippers.ui.screen.integrations.IntegrationsScreen
import com.example.flippers.ui.screen.payment.PaymentHistoryScreen
import com.example.flippers.ui.screen.payment.PaymentMethodScreen
import com.example.flippers.ui.screen.pdftools.CompressPdfScreen
import com.example.flippers.ui.screen.pdftools.MergePdfScreen
import com.example.flippers.ui.screen.pdftools.PdfToolsScreen
import com.example.flippers.ui.screen.pdftools.WatermarkScreen
import com.example.flippers.ui.screen.qr.QrContentInputScreen
import com.example.flippers.ui.screen.qr.QrDetailScreen
import com.example.flippers.ui.screen.qr.QrGeneratorScreen
import com.example.flippers.ui.screen.qr.QrHistoryScreen
import com.example.flippers.ui.screen.qr.QrPreviewScreen
import com.example.flippers.ui.screen.scanner.BookScannerScreen
import com.example.flippers.ui.screen.scanner.DocumentScannerScreen
import com.example.flippers.ui.screen.scanner.IdCardScannerScreen
import com.example.flippers.ui.screen.scanner.QrCodeScannerScreen
import com.example.flippers.ui.screen.scanner.ScanResultScreen
import com.example.flippers.ui.screen.settings.SettingsScreen
import com.example.flippers.ui.screen.subscription.PaywallScreen
import com.example.flippers.ui.screen.subscription.SubscriptionScreen
import com.example.flippers.ui.screen.subscription.UsageDashboardScreen
import com.example.flippers.ui.screen.qr.EventQrEditorScreen
import com.example.flippers.ui.screen.qr.EventQrScreen
import com.example.flippers.viewmodel.EventQrViewModel
import com.example.flippers.viewmodel.QrGeneratorViewModel

// bottomNavRoutes is imported from BottomNavBar.kt

@Composable
fun FlippersNavGraph(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onItemClick = { item ->
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    onScanClick = {
                        navController.navigate(Screen.DocumentScanner.route)
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {

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

            composable(Screen.Home.route) {
                HomeScreen(
                    onScanClick = { navController.navigate(Screen.DocumentScanner.route) },
                    onQrScanClick = { navController.navigate(Screen.QrCodeScanner.route) },
                    onGenerateQrClick = { navController.navigate(Screen.QrGenerator.route) },
                    onPdfToolsClick = { navController.navigate(Screen.PdfTools.route) },
                    onAnalyticsClick = { navController.navigate(Screen.AnalyticsDashboard.route) },
                    onSettingsClick = { navController.navigate(Screen.Settings.route) },
                    onProfileClick = { navController.navigate(Screen.Profile.route) }
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

            // ---- QR Generation ----

            composable(Screen.QrGenerator.route) {
                QrGeneratorScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onTypeSelected = { type ->
                        navController.navigate(Screen.QrContentInput.createRoute(type))
                    },
                    onHistoryClick = { navController.navigate(Screen.QrHistory.route) },
                    onEventQrClick = { navController.navigate(Screen.EventQrSelector.route) }
                )
            }

            composable(
                route = Screen.QrContentInput.route,
                arguments = listOf(navArgument("qrType") { type = NavType.StringType })
            ) { backStackEntry ->
                val qrType = backStackEntry.arguments?.getString("qrType") ?: ""
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.QrGenerator.route)
                }
                val qrViewModel: QrGeneratorViewModel = viewModel(parentEntry)
                androidx.compose.runtime.LaunchedEffect(qrType) {
                    qrViewModel.selectType(qrType)
                }
                QrContentInputScreen(
                    qrType = qrType,
                    viewModel = qrViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onGenerate = {
                        val bitmap = qrViewModel.generateQrCode()
                        if (bitmap != null) {
                            navController.navigate(Screen.QrPreview.route)
                        }
                    }
                )
            }

            composable(Screen.QrPreview.route) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(Screen.QrGenerator.route)
                }
                val qrViewModel: QrGeneratorViewModel = viewModel(parentEntry)
                QrPreviewScreen(
                    viewModel = qrViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.QrHistory.route) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(Screen.QrGenerator.route)
                }
                val qrViewModel: QrGeneratorViewModel = viewModel(parentEntry)
                QrHistoryScreen(
                    viewModel = qrViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onItemClick = { qrCodeId ->
                        navController.navigate(Screen.QrDetail.createRoute(qrCodeId))
                    }
                )
            }

            composable(
                route = Screen.QrDetail.route,
                arguments = listOf(navArgument("qrCodeId") { type = NavType.LongType })
            ) { backStackEntry ->
                val qrCodeId = backStackEntry.arguments?.getLong("qrCodeId") ?: 0L
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.QrGenerator.route)
                }
                val qrViewModel: QrGeneratorViewModel = viewModel(parentEntry)
                QrDetailScreen(
                    qrCodeId = qrCodeId,
                    viewModel = qrViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ---- Event QR ----

            composable(Screen.EventQrSelector.route) {
                val eventQrViewModel: EventQrViewModel = viewModel(it)
                EventQrScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onEventTypeSelected = { eventType ->
                        eventQrViewModel.selectEventType(eventType)
                        navController.navigate(Screen.EventQrEditor.route)
                    }
                )
            }

            composable(Screen.EventQrEditor.route) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(Screen.EventQrSelector.route)
                }
                val eventQrViewModel: EventQrViewModel = viewModel(parentEntry)
                EventQrEditorScreen(
                    viewModel = eventQrViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ---- Subscription ----

            composable(Screen.Subscription.route) {
                SubscriptionScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.Paywall.route) {
                PaywallScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onViewPlans = { navController.navigate(Screen.Subscription.route) }
                )
            }

            composable(Screen.UsageDashboard.route) {
                UsageDashboardScreen(onNavigateBack = { navController.popBackStack() })
            }

            // ---- Analytics ----

            composable(Screen.AnalyticsDashboard.route) {
                AnalyticsDashboardScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onViewScanStats = { navController.navigate(Screen.ScanStats.route) }
                )
            }

            composable(Screen.ScanStats.route) {
                ScanStatsScreen(onNavigateBack = { navController.popBackStack() })
            }

            // ---- Enterprise ----

            composable(Screen.AdminDashboard.route) {
                AdminDashboardScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onTeamClick = { navController.navigate(Screen.TeamManagement.route) },
                    onAuditClick = { navController.navigate(Screen.AuditLog.route) },
                    onSecurityClick = { navController.navigate(Screen.SecuritySettings.route) }
                )
            }

            composable(Screen.TeamManagement.route) {
                TeamManagementScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.AuditLog.route) {
                AuditLogScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.SecuritySettings.route) {
                SecuritySettingsScreen(onNavigateBack = { navController.popBackStack() })
            }

            // ---- Integrations ----

            composable(Screen.Integrations.route) {
                IntegrationsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onIntegrationClick = { name ->
                        navController.navigate(Screen.IntegrationDetail.createRoute(name))
                    }
                )
            }

            composable(
                route = Screen.IntegrationDetail.route,
                arguments = listOf(navArgument("integrationName") { type = NavType.StringType })
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("integrationName") ?: ""
                IntegrationDetailScreen(
                    integrationName = name,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ---- Payment ----

            composable(Screen.PaymentMethods.route) {
                PaymentMethodScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.PaymentHistory.route) {
                PaymentHistoryScreen(onNavigateBack = { navController.popBackStack() })
            }

            // ---- Settings ----

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onProfileClick = { navController.navigate(Screen.Profile.route) },
                    onSubscriptionClick = { navController.navigate(Screen.Subscription.route) },
                    onPaymentMethodsClick = { navController.navigate(Screen.PaymentMethods.route) },
                    onUsageDashboardClick = { navController.navigate(Screen.UsageDashboard.route) },
                    onAdminDashboardClick = { navController.navigate(Screen.AdminDashboard.route) },
                    onIntegrationsClick = { navController.navigate(Screen.Integrations.route) }
                )
            }

            // ---- PDF Tools ----

            composable(Screen.PdfTools.route) {
                PdfToolsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onWatermarkClick = { navController.navigate(Screen.Watermark.route) },
                    onMergeClick = { navController.navigate(Screen.MergePdf.route) },
                    onCompressClick = { navController.navigate(Screen.CompressPdf.route) }
                )
            }

            composable(Screen.Watermark.route) {
                WatermarkScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.MergePdf.route) {
                MergePdfScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.CompressPdf.route) {
                CompressPdfScreen(onNavigateBack = { navController.popBackStack() })
            }

            // ---- Profile ----

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSettingsClick = { navController.navigate(Screen.Settings.route) },
                    onSubscriptionClick = { navController.navigate(Screen.Subscription.route) },
                    onPaymentHistoryClick = { navController.navigate(Screen.PaymentHistory.route) }
                )
            }
        }
    }
}
