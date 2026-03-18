package com.example.flippers.ui.screen.auth

import androidx.compose.runtime.Composable
import com.example.flippers.ui.components.SuccessDialog

@Composable
fun ResetPasswordSuccessScreen(onGoToSignIn: () -> Unit) {
    SuccessDialog(
        title = "Password Reset Successful!",
        message = "Your password has been reset successfully.",
        buttonText = "Go to Sign In",
        onButtonClick = onGoToSignIn
    )
}
