package com.example.proscan.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val user: UserResponse
)

@Serializable
data class TokenPairResponse(
    val token: String,
    val refreshToken: String
)

@Serializable
data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val dateOfBirth: String? = null,
    val plan: String,
    val profileCompleted: Boolean
)
