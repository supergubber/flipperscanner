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
}
