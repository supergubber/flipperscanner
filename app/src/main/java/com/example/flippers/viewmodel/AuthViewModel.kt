package com.example.flippers.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flippers.data.local.ProScanDatabase
import com.example.flippers.data.preferences.ProScanPreferences
import com.example.flippers.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuthRepository
    private val preferences = ProScanPreferences(application)

    // Sign Up fields
    private val _signUpEmail = MutableStateFlow("")
    val signUpEmail: StateFlow<String> = _signUpEmail.asStateFlow()

    private val _signUpPassword = MutableStateFlow("")
    val signUpPassword: StateFlow<String> = _signUpPassword.asStateFlow()

    private val _signUpConfirmPassword = MutableStateFlow("")
    val signUpConfirmPassword: StateFlow<String> = _signUpConfirmPassword.asStateFlow()

    // Sign In fields
    private val _signInEmail = MutableStateFlow("")
    val signInEmail: StateFlow<String> = _signInEmail.asStateFlow()

    private val _signInPassword = MutableStateFlow("")
    val signInPassword: StateFlow<String> = _signInPassword.asStateFlow()

    // Forgot password
    private val _forgotEmail = MutableStateFlow("")
    val forgotEmail: StateFlow<String> = _forgotEmail.asStateFlow()

    // New password
    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmNewPassword = MutableStateFlow("")
    val confirmNewPassword: StateFlow<String> = _confirmNewPassword.asStateFlow()

    // Remember me
    private val _rememberMe = MutableStateFlow(false)
    val rememberMe: StateFlow<Boolean> = _rememberMe.asStateFlow()

    // Password visibility
    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    // Loading & error
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Events
    private val _signUpSuccess = MutableSharedFlow<Unit>()
    val signUpSuccess: SharedFlow<Unit> = _signUpSuccess.asSharedFlow()

    private val _signInSuccess = MutableSharedFlow<Unit>()
    val signInSuccess: SharedFlow<Unit> = _signInSuccess.asSharedFlow()

    private val _resetPasswordSuccess = MutableSharedFlow<Unit>()
    val resetPasswordSuccess: SharedFlow<Unit> = _resetPasswordSuccess.asSharedFlow()

    init {
        val dao = ProScanDatabase.getDatabase(application).userDao()
        repository = AuthRepository(dao)
    }

    fun onSignUpEmailChange(value: String) { _signUpEmail.value = value }
    fun onSignUpPasswordChange(value: String) { _signUpPassword.value = value }
    fun onSignUpConfirmPasswordChange(value: String) { _signUpConfirmPassword.value = value }
    fun onSignInEmailChange(value: String) { _signInEmail.value = value }
    fun onSignInPasswordChange(value: String) { _signInPassword.value = value }
    fun onForgotEmailChange(value: String) { _forgotEmail.value = value }
    fun onNewPasswordChange(value: String) { _newPassword.value = value }
    fun onConfirmNewPasswordChange(value: String) { _confirmNewPassword.value = value }
    fun onRememberMeChange(value: Boolean) { _rememberMe.value = value }
    fun togglePasswordVisibility() { _passwordVisible.value = !_passwordVisible.value }
    fun clearError() { _errorMessage.value = null }

    fun signUp() {
        val email = _signUpEmail.value.trim()
        val password = _signUpPassword.value
        val confirm = _signUpConfirmPassword.value

        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Please fill all fields"
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Invalid email format"
            return
        }
        if (password.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters"
            return
        }
        if (password != confirm) {
            _errorMessage.value = "Passwords do not match"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.signUp(email, password)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    preferences.setUserEmail(email)
                    if (_rememberMe.value) {
                        preferences.setLoggedIn(true)
                    }
                    _signUpSuccess.emit(Unit)
                },
                onFailure = { _errorMessage.value = it.message }
            )
        }
    }

    fun signIn() {
        val email = _signInEmail.value.trim()
        val password = _signInPassword.value

        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Please fill all fields"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.signIn(email, password)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    preferences.setUserEmail(email)
                    preferences.setLoggedIn(true)
                    _signInSuccess.emit(Unit)
                },
                onFailure = { _errorMessage.value = it.message }
            )
        }
    }

    fun resetPassword(email: String) {
        val password = _newPassword.value
        val confirm = _confirmNewPassword.value

        if (password.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters"
            return
        }
        if (password != confirm) {
            _errorMessage.value = "Passwords do not match"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.resetPassword(email, password)
            _isLoading.value = false
            result.fold(
                onSuccess = { _resetPasswordSuccess.emit(Unit) },
                onFailure = { _errorMessage.value = it.message }
            )
        }
    }
}
