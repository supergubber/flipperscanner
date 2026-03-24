package com.example.proscan.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class AppConfigResponse(
    val theme: ThemeConfig,
    val bottomNavTabs: List<NavTabConfig>,
    val centerButton: NavTabConfig,
    val bottomNavVisibleOn: List<String>,
    val homeQuickActions: List<QuickActionConfig>,
    val scannerTypes: List<ToolItemConfig>,
    val qrTypes: List<ToolItemConfig>,
    val pdfTools: List<ToolItemConfig>,
    val subscriptionPlans: List<SubscriptionPlanResponse>,
    val featureFlags: FeatureFlagsConfig,
    val settingsSections: List<SettingsSectionConfig>,
    val integrations: List<IntegrationCatalogItem>
)

@Serializable
data class ThemeConfig(
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
data class NavTabConfig(
    val label: String,
    val route: String,
    val icon: String
)

@Serializable
data class QuickActionConfig(
    val icon: String,
    val title: String,
    val subtitle: String,
    val color: String,
    val route: String
)

@Serializable
data class ToolItemConfig(
    val key: String,
    val label: String,
    val icon: String,
    val color: String? = null,
    val route: String? = null
)

@Serializable
data class SubscriptionPlanResponse(
    val name: String,
    val price: String,
    val highlight: Boolean = false,
    val features: List<PlanFeatureResponse>
)

@Serializable
data class PlanFeatureResponse(
    val name: String,
    val value: String
)

@Serializable
data class FeatureFlagsConfig(
    val showUpgradeBanner: Boolean = true,
    val enablePdfTools: Boolean = true,
    val enableEnterprise: Boolean = true,
    val enableAnalytics: Boolean = true,
    val enableIntegrations: Boolean = true,
    val enableCloudBackup: Boolean = false
)

@Serializable
data class SettingsSectionConfig(
    val title: String,
    val items: List<SettingsItemConfig>
)

@Serializable
data class SettingsItemConfig(
    val icon: String,
    val label: String,
    val color: String,
    val route: String
)

@Serializable
data class IntegrationCatalogItem(
    val name: String,
    val icon: String,
    val description: String,
    val category: String
)

// --- Other feature responses ---

@Serializable
data class DocumentResponse(
    val id: Long,
    val fileName: String,
    val fileType: String,
    val fileSizeBytes: Long,
    val scanType: String,
    val mimeType: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class QrCodeResponse(
    val id: Long,
    val type: String,
    val content: String,
    val label: String,
    val imagePath: String?,
    val createdAt: String
)

@Serializable
data class UsageStatsResponse(
    val scansUsed: Int,
    val scansLimit: Int,
    val qrUsed: Int,
    val qrLimit: Int,
    val storageUsedMb: Int,
    val storageLimitMb: Int
)

@Serializable
data class CurrentSubscriptionResponse(
    val plan: String,
    val usage: UsageStatsResponse
)

@Serializable
data class AnalyticsDashboardResponse(
    val totalScans: Int,
    val dailyAverage: Int,
    val mostUsedType: String,
    val todayScans: Int,
    val weeklyScans: List<Int>,
    val weekDays: List<String>,
    val scanTypeBreakdown: List<ScanTypeStatResponse>
)

@Serializable
data class ScanTypeStatResponse(
    val type: String,
    val count: Int
)

@Serializable
data class TeamMemberResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role: String,
    val avatarInitial: String,
    val joinedDate: String
)

@Serializable
data class OrgStatsResponse(
    val name: String,
    val members: Int,
    val totalScans: Int,
    val plan: String
)

@Serializable
data class AuditEntryResponse(
    val id: Long,
    val user: String,
    val action: String,
    val timestamp: String
)

@Serializable
data class SecuritySettingsResponse(
    val require2fa: Boolean,
    val passwordMinLength: Int,
    val sessionTimeoutMinutes: Int,
    val dataRetention: String = "90 days",
    val autoLockTimeout: String = "5 minutes"
)

@Serializable
data class IntegrationConfigResponse(
    val integrationId: Long,
    val integrationName: String,
    val apiKey: String = "",
    val webhookUrl: String = ""
)

@Serializable
data class UserStatsResponse(
    val totalScans: Int,
    val totalQrCodes: Int,
    val plan: String
)

@Serializable
data class UserPreferencesResponse(
    val notifications: Boolean = true,
    val autoSave: Boolean = true,
    val cloudBackup: Boolean = false
)

@Serializable
data class IntegrationResponse(
    val id: Long,
    val name: String,
    val icon: String,
    val description: String,
    val isConnected: Boolean,
    val category: String
)

@Serializable
data class PaymentMethodResponse(
    val id: Long,
    val name: String,
    val icon: String,
    val lastFour: String,
    val isDefault: Boolean
)

@Serializable
data class TransactionResponse(
    val id: Long,
    val description: String,
    val amount: String,
    val date: String,
    val status: String
)
