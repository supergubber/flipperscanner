package com.example.flippers.data.remote.dto

import kotlinx.serialization.Serializable

// ── Subscriptions ──

@Serializable
data class SubscriptionPlanDto(
    val name: String,
    val price: String,
    val highlight: Boolean = false,
    val features: List<PlanFeatureDto>
)

@Serializable
data class PlanFeatureDto(val name: String, val value: String)

@Serializable
data class CurrentSubscriptionDto(val plan: String, val usage: UsageStatsDto)

@Serializable
data class UsageStatsDto(
    val scansUsed: Int, val scansLimit: Int,
    val qrUsed: Int, val qrLimit: Int,
    val storageUsedMb: Int, val storageLimitMb: Int
)

@Serializable
data class UpgradeRequest(val plan: String)

// ── Analytics ──

@Serializable
data class AnalyticsDashboardDto(
    val totalScans: Int,
    val dailyAverage: Int,
    val mostUsedType: String,
    val todayScans: Int,
    val weeklyScans: List<Int>,
    val weekDays: List<String>,
    val scanTypeBreakdown: List<ScanTypeStatDto>
)

@Serializable
data class ScanTypeStatDto(val type: String, val count: Int)

// ── QR Codes ──

@Serializable
data class QrCodeDto(
    val id: Long, val type: String, val content: String,
    val label: String, val imagePath: String?, val createdAt: String
)

@Serializable
data class CreateQrRequest(val type: String, val content: String, val label: String)

// ── Documents ──

@Serializable
data class DocumentDto(
    val id: Long, val fileName: String, val fileType: String,
    val fileSizeBytes: Long, val scanType: String, val mimeType: String,
    val createdAt: String, val updatedAt: String
)

@Serializable
data class PaginatedDocumentsDto(
    val success: Boolean = true,
    val data: List<DocumentDto>,
    val page: Int, val size: Int, val total: Long
)

// ── Enterprise ──

@Serializable
data class OrgStatsDto(val name: String, val members: Int, val totalScans: Int, val plan: String)

@Serializable
data class TeamMemberDto(
    val id: Long, val name: String, val email: String,
    val role: String, val avatarInitial: String, val joinedDate: String
)

@Serializable
data class CreateTeamMemberRequest(val name: String, val email: String, val role: String)

@Serializable
data class UpdateTeamMemberRequest(val role: String)

@Serializable
data class AuditEntryDto(val id: Long, val user: String, val action: String, val timestamp: String)

@Serializable
data class PaginatedAuditLogDto(
    val success: Boolean = true,
    val data: List<AuditEntryDto>,
    val page: Int, val size: Int, val total: Long
)

@Serializable
data class SecuritySettingsDto(
    val require2fa: Boolean, val passwordMinLength: Int,
    val sessionTimeoutMinutes: Int,
    val dataRetention: String = "90 days",
    val autoLockTimeout: String = "5 minutes"
)

@Serializable
data class UpdateSecurityRequest(
    val require2fa: Boolean = false, val passwordMinLength: Int = 8,
    val sessionTimeoutMinutes: Int = 30,
    val dataRetention: String = "90 days", val autoLockTimeout: String = "5 minutes"
)

// ── Integrations ──

@Serializable
data class IntegrationDto(
    val id: Long, val name: String, val icon: String,
    val description: String, val isConnected: Boolean, val category: String
)

@Serializable
data class IntegrationConfigDto(
    val integrationId: Long, val integrationName: String,
    val apiKey: String = "", val webhookUrl: String = ""
)

@Serializable
data class IntegrationConfigRequest(val apiKey: String = "", val webhookUrl: String = "")

// ── Payments ──

@Serializable
data class PaymentMethodDto(
    val id: Long, val name: String, val icon: String,
    val lastFour: String, val isDefault: Boolean
)

@Serializable
data class CreatePaymentMethodRequest(val name: String, val icon: String, val lastFour: String = "")

@Serializable
data class AddCardRequest(val cardNumber: String, val expiry: String, val cvv: String)

@Serializable
data class TransactionDto(
    val id: Long, val description: String, val amount: String,
    val date: String, val status: String
)
