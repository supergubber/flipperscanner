package com.example.proscan.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateQrRequest(
    val type: String,
    val content: String,
    val label: String
)

@Serializable
data class CreateTeamMemberRequest(
    val name: String,
    val email: String,
    val role: String
)

@Serializable
data class UpdateTeamMemberRequest(
    val role: String
)

@Serializable
data class CreatePaymentMethodRequest(
    val name: String,
    val icon: String,
    val lastFour: String = ""
)

@Serializable
data class UpgradeRequest(
    val plan: String
)

@Serializable
data class UpdateSecurityRequest(
    val require2fa: Boolean = false,
    val passwordMinLength: Int = 8,
    val sessionTimeoutMinutes: Int = 30,
    val dataRetention: String = "90 days",
    val autoLockTimeout: String = "5 minutes"
)

@Serializable
data class IntegrationConfigRequest(
    val apiKey: String = "",
    val webhookUrl: String = ""
)

@Serializable
data class AddCardRequest(
    val cardNumber: String,
    val expiry: String,
    val cvv: String
)

@Serializable
data class UpdatePreferencesRequest(
    val notifications: Boolean? = null,
    val autoSave: Boolean? = null,
    val cloudBackup: Boolean? = null
)

@Serializable
data class WatermarkRequest(
    val text: String,
    val position: String = "center"
)
