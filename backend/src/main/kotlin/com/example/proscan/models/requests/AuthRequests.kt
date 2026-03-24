package com.example.proscan.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RefreshRequest(
    val refreshToken: String
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val otp: String,
    val newPassword: String
)

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val avatarUrl: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val dateOfBirth: String? = null
)
