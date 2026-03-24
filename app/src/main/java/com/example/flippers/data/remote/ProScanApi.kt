package com.example.flippers.data.remote

import com.example.flippers.data.remote.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProScanApi {

    // ── Auth ──
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<TokenPairResponse>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<MessageResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<MessageResponse>

    @GET("auth/profile")
    suspend fun getProfile(): Response<UserProfileDto>

    @PUT("auth/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserProfileDto>

    @POST("auth/logout")
    suspend fun logout(): Response<MessageResponse>

    @DELETE("auth/account")
    suspend fun deleteAccount(): Response<MessageResponse>

    @GET("auth/stats")
    suspend fun getUserStats(): Response<UserStatsDto>

    @GET("auth/preferences")
    suspend fun getPreferences(): Response<UserPreferencesDto>

    @PUT("auth/preferences")
    suspend fun updatePreferences(@Body request: UpdatePreferencesRequest): Response<UserPreferencesDto>

    // ── Config (SDUI) ──
    @GET("config")
    suspend fun getAppConfig(): Response<AppConfigDto>

    // ── Subscriptions ──
    @GET("subscriptions/plans")
    suspend fun getPlans(): Response<List<SubscriptionPlanDto>>

    @GET("subscriptions/current")
    suspend fun getCurrentSubscription(): Response<CurrentSubscriptionDto>

    @PUT("subscriptions/upgrade")
    suspend fun upgradePlan(@Body request: UpgradeRequest): Response<CurrentSubscriptionDto>

    @GET("subscriptions/usage")
    suspend fun getUsage(): Response<UsageStatsDto>

    // ── Analytics ──
    @GET("analytics/dashboard")
    suspend fun getAnalyticsDashboard(): Response<AnalyticsDashboardDto>

    @GET("analytics/scan-stats")
    suspend fun getScanStats(@Query("period") period: String = "week"): Response<AnalyticsDashboardDto>

    // ── QR Codes ──
    @POST("qr")
    suspend fun createQrCode(@Body request: CreateQrRequest): Response<QrCodeDto>

    @GET("qr")
    suspend fun getQrCodes(): Response<List<QrCodeDto>>

    @GET("qr/history")
    suspend fun getQrHistory(): Response<List<QrCodeDto>>

    @GET("qr/{id}")
    suspend fun getQrCode(@Path("id") id: Long): Response<QrCodeDto>

    @DELETE("qr/{id}")
    suspend fun deleteQrCode(@Path("id") id: Long): Response<MessageResponse>

    // ── Documents ──
    @Multipart
    @POST("documents/upload")
    suspend fun uploadDocument(
        @Part file: MultipartBody.Part,
        @Part("scanType") scanType: RequestBody
    ): Response<DocumentDto>

    @GET("documents")
    suspend fun getDocuments(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
        @Query("search") search: String? = null
    ): Response<PaginatedDocumentsDto>

    @GET("documents/{id}")
    suspend fun getDocument(@Path("id") id: Long): Response<DocumentDto>

    @DELETE("documents/{id}")
    suspend fun deleteDocument(@Path("id") id: Long): Response<MessageResponse>

    // ── Enterprise ──
    @GET("enterprise/dashboard")
    suspend fun getEnterpriseDashboard(): Response<OrgStatsDto>

    @GET("enterprise/team")
    suspend fun getTeamMembers(): Response<List<TeamMemberDto>>

    @POST("enterprise/team")
    suspend fun addTeamMember(@Body request: CreateTeamMemberRequest): Response<TeamMemberDto>

    @PUT("enterprise/team/{id}")
    suspend fun updateTeamMember(@Path("id") id: Long, @Body request: UpdateTeamMemberRequest): Response<TeamMemberDto>

    @DELETE("enterprise/team/{id}")
    suspend fun removeTeamMember(@Path("id") id: Long): Response<MessageResponse>

    @GET("enterprise/audit-log")
    suspend fun getAuditLog(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 50
    ): Response<PaginatedAuditLogDto>

    @GET("enterprise/security")
    suspend fun getSecuritySettings(): Response<SecuritySettingsDto>

    @PUT("enterprise/security")
    suspend fun updateSecuritySettings(@Body request: UpdateSecurityRequest): Response<SecuritySettingsDto>

    // ── Integrations ──
    @GET("integrations")
    suspend fun getIntegrations(): Response<List<IntegrationDto>>

    @POST("integrations/{id}/connect")
    suspend fun connectIntegration(@Path("id") id: Long): Response<IntegrationDto>

    @POST("integrations/{id}/disconnect")
    suspend fun disconnectIntegration(@Path("id") id: Long): Response<IntegrationDto>

    @GET("integrations/{id}/config")
    suspend fun getIntegrationConfig(@Path("id") id: Long): Response<IntegrationConfigDto>

    @POST("integrations/{id}/configure")
    suspend fun saveIntegrationConfig(@Path("id") id: Long, @Body request: IntegrationConfigRequest): Response<IntegrationConfigDto>

    // ── Payments ──
    @GET("payments/methods")
    suspend fun getPaymentMethods(): Response<List<PaymentMethodDto>>

    @POST("payments/methods")
    suspend fun addPaymentMethod(@Body request: CreatePaymentMethodRequest): Response<PaymentMethodDto>

    @POST("payments/methods/add-card")
    suspend fun addCard(@Body request: AddCardRequest): Response<PaymentMethodDto>

    @PUT("payments/methods/{id}/default")
    suspend fun setDefaultPaymentMethod(@Path("id") id: Long): Response<PaymentMethodDto>

    @DELETE("payments/methods/{id}")
    suspend fun deletePaymentMethod(@Path("id") id: Long): Response<MessageResponse>

    @GET("payments/history")
    suspend fun getPaymentHistory(): Response<List<TransactionDto>>

    // ── PDF Tools ──
    @Multipart
    @POST("pdf/watermark")
    suspend fun addWatermark(
        @Part file: MultipartBody.Part,
        @Part("text") text: RequestBody
    ): Response<okhttp3.ResponseBody>

    @Multipart
    @POST("pdf/compress")
    suspend fun compressPdf(@Part file: MultipartBody.Part): Response<okhttp3.ResponseBody>

    @Multipart
    @POST("pdf/merge")
    suspend fun mergePdfs(@Part files: List<MultipartBody.Part>): Response<okhttp3.ResponseBody>
}
