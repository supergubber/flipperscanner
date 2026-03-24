package com.example.flippers.data.remote.dto

import kotlinx.serialization.Serializable

// ── Requests ──

@Serializable
data class RegisterRequest(val name: String, val email: String, val password: String)

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class RefreshTokenRequest(val refreshToken: String)

@Serializable
data class ForgotPasswordRequest(val email: String)

@Serializable
data class ResetPasswordRequest(val email: String, val otp: String, val newPassword: String)

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val avatarUrl: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val dateOfBirth: String? = null
)

@Serializable
data class UpdatePreferencesRequest(
    val notifications: Boolean? = null,
    val autoSave: Boolean? = null,
    val cloudBackup: Boolean? = null
)

// ── Responses ──

@Serializable
data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val user: UserProfileDto
)

@Serializable
data class TokenPairResponse(val token: String, val refreshToken: String)

@Serializable
data class MessageResponse(val success: Boolean = true, val message: String)

@Serializable
data class UserProfileDto(
    val id: Long,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val dateOfBirth: String? = null,
    val plan: String = "free",
    val profileCompleted: Boolean = false
)

@Serializable
data class UserStatsDto(
    val totalScans: Int,
    val totalQrCodes: Int,
    val plan: String
)

@Serializable
data class UserPreferencesDto(
    val notifications: Boolean = true,
    val autoSave: Boolean = true,
    val cloudBackup: Boolean = false
)
