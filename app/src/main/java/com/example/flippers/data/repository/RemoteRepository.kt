package com.example.flippers.data.repository

import com.example.flippers.data.remote.ApiClient
import com.example.flippers.data.remote.dto.*

/**
 * Central repository for all backend API calls.
 * Each method tries the backend and returns null on failure (screens can fallback to DummyData).
 */
object RemoteRepository {

    private val api get() = ApiClient.api

    // ── Config ──
    suspend fun getAppConfig(): AppConfigDto? = tryApi { api.getAppConfig() }

    // ── Subscriptions ──
    suspend fun getPlans(): List<SubscriptionPlanDto>? = tryApi { api.getPlans() }
    suspend fun getCurrentSubscription(): CurrentSubscriptionDto? = tryApi { api.getCurrentSubscription() }
    suspend fun upgradePlan(plan: String): CurrentSubscriptionDto? = tryApi { api.upgradePlan(UpgradeRequest(plan)) }
    suspend fun getUsage(): UsageStatsDto? = tryApi { api.getUsage() }

    // ── Analytics ──
    suspend fun getAnalyticsDashboard(): AnalyticsDashboardDto? = tryApi { api.getAnalyticsDashboard() }
    suspend fun getScanStats(period: String = "week"): AnalyticsDashboardDto? = tryApi { api.getScanStats(period) }

    // ── QR ──
    suspend fun getQrHistory(): List<QrCodeDto>? = tryApi { api.getQrHistory() }
    suspend fun createQrCode(type: String, content: String, label: String): QrCodeDto? =
        tryApi { api.createQrCode(CreateQrRequest(type, content, label)) }
    suspend fun deleteQrCode(id: Long): Boolean = tryApiBool { api.deleteQrCode(id) }

    // ── Enterprise ──
    suspend fun getOrgDashboard(): OrgStatsDto? = tryApi { api.getEnterpriseDashboard() }
    suspend fun getTeamMembers(): List<TeamMemberDto>? = tryApi { api.getTeamMembers() }
    suspend fun addTeamMember(name: String, email: String, role: String): TeamMemberDto? =
        tryApi { api.addTeamMember(CreateTeamMemberRequest(name, email, role)) }
    suspend fun updateTeamMember(id: Long, role: String): TeamMemberDto? =
        tryApi { api.updateTeamMember(id, UpdateTeamMemberRequest(role)) }
    suspend fun removeTeamMember(id: Long): Boolean = tryApiBool { api.removeTeamMember(id) }
    suspend fun getAuditLog(page: Int = 1, size: Int = 50): PaginatedAuditLogDto? =
        tryApi { api.getAuditLog(page, size) }
    suspend fun getSecuritySettings(): SecuritySettingsDto? = tryApi { api.getSecuritySettings() }
    suspend fun updateSecuritySettings(req: UpdateSecurityRequest): SecuritySettingsDto? =
        tryApi { api.updateSecuritySettings(req) }

    // ── Integrations ──
    suspend fun getIntegrations(): List<IntegrationDto>? = tryApi { api.getIntegrations() }
    suspend fun connectIntegration(id: Long): IntegrationDto? = tryApi { api.connectIntegration(id) }
    suspend fun disconnectIntegration(id: Long): IntegrationDto? = tryApi { api.disconnectIntegration(id) }
    suspend fun getIntegrationConfig(id: Long): IntegrationConfigDto? = tryApi { api.getIntegrationConfig(id) }
    suspend fun saveIntegrationConfig(id: Long, apiKey: String, webhookUrl: String): IntegrationConfigDto? =
        tryApi { api.saveIntegrationConfig(id, IntegrationConfigRequest(apiKey, webhookUrl)) }

    // ── Payments ──
    suspend fun getPaymentMethods(): List<PaymentMethodDto>? = tryApi { api.getPaymentMethods() }
    suspend fun addCard(cardNumber: String, expiry: String, cvv: String): PaymentMethodDto? =
        tryApi { api.addCard(AddCardRequest(cardNumber, expiry, cvv)) }
    suspend fun setDefaultPaymentMethod(id: Long): PaymentMethodDto? = tryApi { api.setDefaultPaymentMethod(id) }
    suspend fun deletePaymentMethod(id: Long): Boolean = tryApiBool { api.deletePaymentMethod(id) }
    suspend fun getPaymentHistory(): List<TransactionDto>? = tryApi { api.getPaymentHistory() }

    // ── User ──
    suspend fun getUserStats(): UserStatsDto? = tryApi { api.getUserStats() }
    suspend fun getProfile(): UserProfileDto? = tryApi { api.getProfile() }
    suspend fun getPreferences(): UserPreferencesDto? = tryApi { api.getPreferences() }
    suspend fun updatePreferences(req: UpdatePreferencesRequest): UserPreferencesDto? =
        tryApi { api.updatePreferences(req) }
    suspend fun logout(): Boolean = tryApiBool { api.logout() }
    suspend fun deleteAccount(): Boolean = tryApiBool { api.deleteAccount() }

    // ── Helpers ──
    private suspend fun <T> tryApi(call: suspend () -> retrofit2.Response<T>): T? {
        return try {
            val response = call()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun tryApiBool(call: suspend () -> retrofit2.Response<*>): Boolean {
        return try {
            call().isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
