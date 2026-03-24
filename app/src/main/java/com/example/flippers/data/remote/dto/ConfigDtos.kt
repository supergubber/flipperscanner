package com.example.flippers.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppConfigDto(
    val theme: ThemeConfigDto,
    val bottomNavTabs: List<NavTabDto>,
    val centerButton: NavTabDto,
    val bottomNavVisibleOn: List<String>,
    val homeQuickActions: List<QuickActionDto>,
    val scannerTypes: List<ToolItemDto>,
    val qrTypes: List<ToolItemDto>,
    val pdfTools: List<ToolItemDto>,
    val subscriptionPlans: List<SubscriptionPlanDto>,
    val featureFlags: FeatureFlagsDto,
    val settingsSections: List<SettingsSectionDto>,
    val integrations: List<IntegrationCatalogDto>
)

@Serializable
data class ThemeConfigDto(
    val primaryColor: String,
    val primaryDark: String,
    val primaryLight: String,
    val accentBlue: String,
    val accentGreen: String,
    val accentOrange: String,
    val accentPurple: String,
    val accentRed: String,
    val accentTeal: String
)

@Serializable
data class NavTabDto(val label: String, val route: String, val icon: String)

@Serializable
data class QuickActionDto(
    val icon: String, val title: String, val subtitle: String,
    val color: String, val route: String
)

@Serializable
data class ToolItemDto(
    val key: String, val label: String, val icon: String,
    val color: String? = null, val route: String? = null
)

@Serializable
data class FeatureFlagsDto(
    val showUpgradeBanner: Boolean = true,
    val enablePdfTools: Boolean = true,
    val enableEnterprise: Boolean = true,
    val enableAnalytics: Boolean = true,
    val enableIntegrations: Boolean = true,
    val enableCloudBackup: Boolean = false
)

@Serializable
data class SettingsSectionDto(val title: String, val items: List<SettingsItemDto>)

@Serializable
data class SettingsItemDto(val icon: String, val label: String, val color: String, val route: String)

@Serializable
data class IntegrationCatalogDto(
    val name: String, val icon: String, val description: String, val category: String
)
