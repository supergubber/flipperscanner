package com.example.flippers.ui.screen.auth

import androidx.compose.runtime.Composable
import com.example.flippers.ui.components.SuccessDialog

@Composable
fun SignUpSuccessScreen(onGoToHome: () -> Unit) {
    SuccessDialog(
        title = "Sign Up Successful!",
        message = "Your account has been created successfully.",
        buttonText = "Go to Home",
        onButtonClick = onGoToHome
    )
}
