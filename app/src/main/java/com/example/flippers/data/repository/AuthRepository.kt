package com.example.flippers.data.repository

import com.example.flippers.data.local.User
import com.example.flippers.data.local.UserDao
import com.example.flippers.data.remote.ApiClient
import com.example.flippers.data.remote.dto.LoginRequest
import com.example.flippers.data.remote.dto.RegisterRequest
import com.example.flippers.data.remote.dto.ResetPasswordRequest
import com.example.flippers.data.remote.dto.UpdateProfileRequest

class AuthRepository(private val userDao: UserDao) {

    private val api get() = ApiClient.api

    suspend fun signUp(email: String, password: String): Result<User> {
        return try {
            val response = api.register(RegisterRequest(name = email.substringBefore("@"), email = email, password = password))
            if (response.isSuccessful) {
                val body = response.body()!!
                ApiClient.setToken(body.token)
                // Also save locally for offline access
                val user = User(email = email, passwordHash = "", fullName = body.user.name)
                val id = userDao.insertUser(user)
                Result.success(user.copy(id = id))
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            // Fallback to local auth if backend is unreachable
            signUpLocal(email, password)
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(email = email, password = password))
            if (response.isSuccessful) {
                val body = response.body()!!
                ApiClient.setToken(body.token)
                val user = User(
                    email = email, passwordHash = "",
                    fullName = body.user.name,
                    profileCompleted = body.user.profileCompleted
                )
                val id = userDao.insertUser(user)
                Result.success(user.copy(id = id))
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            // Fallback to local auth if backend is unreachable
            signInLocal(email, password)
        }
    }

    suspend fun isEmailTaken(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }

    suspend fun updateUserProfile(
        email: String, fullName: String, phoneNumber: String,
        gender: String, dateOfBirth: String
    ): Result<User> {
        return try {
            val response = api.updateProfile(UpdateProfileRequest(
                name = fullName, phone = phoneNumber, gender = gender, dateOfBirth = dateOfBirth
            ))
            if (response.isSuccessful) {
                val user = userDao.getUserByEmail(email)
                    ?: return Result.failure(Exception("User not found"))
                val updated = user.copy(
                    fullName = fullName, phoneNumber = phoneNumber,
                    gender = gender, dateOfBirth = dateOfBirth, profileCompleted = true
                )
                userDao.updateUser(updated)
                Result.success(updated)
            } else {
                // Fallback to local
                updateProfileLocal(email, fullName, phoneNumber, gender, dateOfBirth)
            }
        } catch (e: Exception) {
            updateProfileLocal(email, fullName, phoneNumber, gender, dateOfBirth)
        }
    }

    suspend fun resetPassword(email: String, newPassword: String): Result<Unit> {
        return try {
            val response = api.resetPassword(ResetPasswordRequest(email = email, otp = "123456", newPassword = newPassword))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Password reset failed"))
            }
        } catch (e: Exception) {
            // Fallback to local
            resetPasswordLocal(email, newPassword)
        }
    }

    // ── Local fallbacks (when backend is unreachable) ──

    private suspend fun signUpLocal(email: String, password: String): Result<User> {
        return try {
            val existing = userDao.getUserByEmail(email)
            if (existing != null) {
                Result.failure(Exception("Email already taken"))
            } else {
                val hash = hashPassword(password)
                val user = User(email = email, passwordHash = hash)
                val id = userDao.insertUser(user)
                Result.success(user.copy(id = id))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun signInLocal(email: String, password: String): Result<User> {
        return try {
            val hash = hashPassword(password)
            val user = userDao.getUserByEmailAndPassword(email, hash)
            if (user != null) Result.success(user)
            else Result.failure(Exception("Invalid email or password"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun updateProfileLocal(
        email: String, fullName: String, phoneNumber: String,
        gender: String, dateOfBirth: String
    ): Result<User> {
        return try {
            val user = userDao.getUserByEmail(email) ?: return Result.failure(Exception("User not found"))
            val updated = user.copy(
                fullName = fullName, phoneNumber = phoneNumber,
                gender = gender, dateOfBirth = dateOfBirth, profileCompleted = true
            )
            userDao.updateUser(updated)
            Result.success(updated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun resetPasswordLocal(email: String, newPassword: String): Result<Unit> {
        return try {
            val user = userDao.getUserByEmail(email) ?: return Result.failure(Exception("User not found"))
            val updated = user.copy(passwordHash = hashPassword(newPassword))
            userDao.updateUser(updated)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun hashPassword(password: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
