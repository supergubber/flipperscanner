package com.example.proscan.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean = true,
    val data: T? = null,
    val message: String? = null
)

@Serializable
data class ErrorResponse(
    val success: Boolean = false,
    val error: String,
    val message: String
)

@Serializable
data class PaginatedResponse<T>(
    val success: Boolean = true,
    val data: List<T>,
    val page: Int,
    val size: Int,
    val total: Long
)

@Serializable
data class MessageResponse(
    val success: Boolean = true,
    val message: String
)
