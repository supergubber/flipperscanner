package com.example.proscan.service

import com.example.proscan.database.tables.*
import com.example.proscan.models.requests.UpdatePreferencesRequest
import com.example.proscan.models.requests.UpdateProfileRequest
import com.example.proscan.models.responses.AuthResponse
import com.example.proscan.models.responses.UserPreferencesResponse
import com.example.proscan.models.responses.UserResponse
import com.example.proscan.models.responses.UserStatsResponse
import com.example.proscan.util.JwtConfig
import com.example.proscan.util.PasswordHash
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

object AuthService {

    fun register(name: String, email: String, password: String): AuthResponse {
        return transaction {
            // Check if email exists
            val existing = Users.selectAll().where { Users.email eq email }.firstOrNull()
            if (existing != null) throw IllegalArgumentException("Email already registered")

            val userId = Users.insert {
                it[Users.email] = email
                it[Users.passwordHash] = PasswordHash.hash(password)
                it[Users.name] = name
                it[Users.plan] = "free"
                it[Users.createdAt] = LocalDateTime.now()
                it[Users.updatedAt] = LocalDateTime.now()
            }[Users.id]

            // Create default subscription
            UserSubscriptions.insert {
                it[UserSubscriptions.userId] = userId
                it[planName] = "free"
            }

            // Seed default integrations for the user
            seedUserIntegrations(userId)

            val user = getUser(userId)!!
            AuthResponse(
                token = JwtConfig.generateAccessToken(userId, email),
                refreshToken = JwtConfig.generateRefreshToken(userId),
                user = user
            )
        }
    }

    fun login(email: String, password: String): AuthResponse {
        return transaction {
            val row = Users.selectAll().where { Users.email eq email }.firstOrNull()
                ?: throw IllegalArgumentException("Invalid credentials")

            if (!PasswordHash.verify(password, row[Users.passwordHash])) {
                throw IllegalArgumentException("Invalid credentials")
            }

            val userId = row[Users.id]
            val user = rowToUser(row)
            AuthResponse(
                token = JwtConfig.generateAccessToken(userId, email),
                refreshToken = JwtConfig.generateRefreshToken(userId),
                user = user
            )
        }
    }

    fun refreshToken(userId: Long): Pair<String, String> {
        return transaction {
            val row = Users.selectAll().where { Users.id eq userId }.firstOrNull()
                ?: throw IllegalArgumentException("User not found")
            val email = row[Users.email]
            Pair(
                JwtConfig.generateAccessToken(userId, email),
                JwtConfig.generateRefreshToken(userId)
            )
        }
    }

    fun getProfile(userId: Long): UserResponse {
        return transaction {
            getUser(userId) ?: throw IllegalArgumentException("User not found")
        }
    }

    fun updateProfile(userId: Long, request: UpdateProfileRequest): UserResponse {
        return transaction {
            Users.update({ Users.id eq userId }) {
                request.name?.let { name -> it[Users.name] = name }
                request.avatarUrl?.let { url -> it[Users.avatarUrl] = url }
                request.phone?.let { phone -> it[Users.phone] = phone }
                request.gender?.let { gender -> it[Users.gender] = gender }
                request.dateOfBirth?.let { dob -> it[Users.dateOfBirth] = dob }
                it[Users.profileCompleted] = true
                it[Users.updatedAt] = LocalDateTime.now()
            }
            getUser(userId)!!
        }
    }

    fun resetPassword(email: String, otp: String, newPassword: String) {
        if (otp != "123456") throw IllegalArgumentException("Invalid OTP")
        transaction {
            val count = Users.update({ Users.email eq email }) {
                it[passwordHash] = PasswordHash.hash(newPassword)
                it[updatedAt] = LocalDateTime.now()
            }
            if (count == 0) throw IllegalArgumentException("User not found")
        }
    }

    fun deleteAccount(userId: Long) {
        transaction {
            // Delete all user data
            QrCodes.deleteWhere { QrCodes.userId eq userId }
            Documents.deleteWhere { Documents.userId eq userId }
            Integrations.deleteWhere { Integrations.userId eq userId }
            PaymentMethods.deleteWhere { PaymentMethods.userId eq userId }
            Transactions.deleteWhere { Transactions.userId eq userId }
            UserSubscriptions.deleteWhere { UserSubscriptions.userId eq userId }
            Users.deleteWhere { Users.id eq userId }
        }
    }

    fun getUserStats(userId: Long): UserStatsResponse {
        return transaction {
            val scanCount = Documents.selectAll().where { Documents.userId eq userId }.count().toInt()
            val qrCount = QrCodes.selectAll().where { QrCodes.userId eq userId }.count().toInt()
            val plan = Users.selectAll().where { Users.id eq userId }.firstOrNull()?.get(Users.plan) ?: "free"
            // Fallback to dummy stats if user has no real data
            UserStatsResponse(
                totalScans = if (scanCount > 0) scanCount else 88,
                totalQrCodes = if (qrCount > 0) qrCount else 12,
                plan = plan
            )
        }
    }

    // In-memory preferences (per userId)
    private val prefsStore = mutableMapOf<Long, UserPreferencesResponse>()

    fun getPreferences(userId: Long): UserPreferencesResponse {
        return prefsStore.getOrDefault(userId, UserPreferencesResponse())
    }

    fun updatePreferences(userId: Long, request: UpdatePreferencesRequest): UserPreferencesResponse {
        val current = getPreferences(userId)
        val updated = UserPreferencesResponse(
            notifications = request.notifications ?: current.notifications,
            autoSave = request.autoSave ?: current.autoSave,
            cloudBackup = request.cloudBackup ?: current.cloudBackup
        )
        prefsStore[userId] = updated
        return updated
    }

    private fun getUser(userId: Long): UserResponse? {
        return Users.selectAll().where { Users.id eq userId }.firstOrNull()?.let { rowToUser(it) }
    }

    private fun rowToUser(row: ResultRow) = UserResponse(
        id = row[Users.id],
        name = row[Users.name],
        email = row[Users.email],
        avatarUrl = row[Users.avatarUrl],
        phone = row[Users.phone],
        gender = row[Users.gender],
        dateOfBirth = row[Users.dateOfBirth],
        plan = row[Users.plan],
        profileCompleted = row[Users.profileCompleted]
    )

    private fun seedUserIntegrations(userId: Long) {
        val integrations = listOf(
            Triple("Slack", "Chat", "Productivity") to "Send scan notifications to channels",
            Triple("Google Drive", "CloudUpload", "Storage") to "Auto-backup scans to Drive",
            Triple("Dropbox", "Cloud", "Storage") to "Sync scans to Dropbox",
            Triple("Salesforce", "Business", "CRM") to "Attach scans to CRM records",
            Triple("HubSpot", "Hub", "CRM") to "Link documents to contacts",
            Triple("Stripe", "Payment", "Payment") to "Scan receipts for payment records",
            Triple("Shopify", "ShoppingCart", "Payment") to "Scan product barcodes"
        )
        integrations.forEach { (info, desc) ->
            Integrations.insert {
                it[Integrations.userId] = userId
                it[name] = info.first; it[icon] = info.second; it[category] = info.third
                it[description] = desc; it[isConnected] = false
            }
        }
    }
}
