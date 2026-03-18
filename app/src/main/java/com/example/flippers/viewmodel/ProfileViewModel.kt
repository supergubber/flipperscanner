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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuthRepository
    private val preferences = ProScanPreferences(application)

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender.asStateFlow()

    private val _dateOfBirth = MutableStateFlow("")
    val dateOfBirth: StateFlow<String> = _dateOfBirth.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _profileSaved = MutableSharedFlow<Unit>()
    val profileSaved: SharedFlow<Unit> = _profileSaved.asSharedFlow()

    init {
        val dao = ProScanDatabase.getDatabase(application).userDao()
        repository = AuthRepository(dao)
    }

    fun onFullNameChange(value: String) { _fullName.value = value }
    fun onPhoneNumberChange(value: String) { _phoneNumber.value = value }
    fun onGenderChange(value: String) { _gender.value = value }
    fun onDateOfBirthChange(value: String) { _dateOfBirth.value = value }

    fun saveProfile() {
        if (_fullName.value.isBlank()) {
            _errorMessage.value = "Full name is required"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val email = preferences.userEmail.first()
            val result = repository.updateUserProfile(
                email = email,
                fullName = _fullName.value,
                phoneNumber = _phoneNumber.value,
                gender = _gender.value,
                dateOfBirth = _dateOfBirth.value
            )
            _isLoading.value = false
            result.fold(
                onSuccess = { _profileSaved.emit(Unit) },
                onFailure = { _errorMessage.value = it.message }
            )
        }
    }
}
