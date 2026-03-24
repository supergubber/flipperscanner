package com.example.flippers.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object SplashFeatures : Screen("splash_features")
    data object SplashReady : Screen("splash_ready")
    data object Onboarding : Screen("onboarding")
    data object LetYouIn : Screen("let_you_in")
    data object SignUp : Screen("sign_up")
    data object SignUpSuccess : Screen("sign_up_success")
    data object SignIn : Screen("sign_in")
    data object ForgotPassword : Screen("forgot_password")
    data object OtpVerification : Screen("otp_verification/{email}") {
        fun createRoute(email: String) = "otp_verification/$email"
    }
    data object CreateNewPassword : Screen("create_new_password/{email}") {
        fun createRoute(email: String) = "create_new_password/$email"
    }
    data object ResetPasswordSuccess : Screen("reset_password_success")
    data object ProfileSetup : Screen("profile_setup")
    data object Home : Screen("home")
    data object QrCodeScanner : Screen("qr_code_scanner")
    data object DocumentScanner : Screen("document_scanner")
    data object BookScanner : Screen("book_scanner")
    data object IdCardScanner : Screen("id_card_scanner")
    data object ScanResult : Screen("scan_result/{imagePath}") {
        fun createRoute(imagePath: String) = "scan_result/${java.net.URLEncoder.encode(imagePath, "UTF-8")}"
    }

    // QR Generation
    data object QrGenerator : Screen("qr_generator")
    data object QrContentInput : Screen("qr_content_input/{qrType}") {
        fun createRoute(qrType: String) = "qr_content_input/$qrType"
    }
    data object QrPreview : Screen("qr_preview")
    data object QrHistory : Screen("qr_history")

    // Subscription
    data object Subscription : Screen("subscription")
    data object Paywall : Screen("paywall")
    data object UsageDashboard : Screen("usage_dashboard")

    // Analytics
    data object AnalyticsDashboard : Screen("analytics_dashboard")
    data object ScanStats : Screen("scan_stats")

    // Enterprise
    data object AdminDashboard : Screen("admin_dashboard")
    data object TeamManagement : Screen("team_management")
    data object AuditLog : Screen("audit_log")
    data object SecuritySettings : Screen("security_settings")

    // Integrations
    data object Integrations : Screen("integrations")
    data object IntegrationDetail : Screen("integration_detail/{integrationName}") {
        fun createRoute(name: String) = "integration_detail/$name"
    }

    // Payment
    data object PaymentMethods : Screen("payment_methods")
    data object PaymentHistory : Screen("payment_history")

    // Settings
    data object Settings : Screen("settings")

    // PDF Tools
    data object PdfTools : Screen("pdf_tools")
    data object Watermark : Screen("watermark")
    data object MergePdf : Screen("merge_pdf")
    data object CompressPdf : Screen("compress_pdf")

    // Profile (enhanced)
    data object Profile : Screen("profile")
}
