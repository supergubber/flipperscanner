package com.example.flippers.data.repository

import com.example.flippers.data.local.User
import com.example.flippers.data.local.UserDao
import java.security.MessageDigest

class AuthRepository(private val userDao: UserDao) {

    suspend fun signUp(email: String, password: String): Result<User> {
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

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val hash = hashPassword(password)
            val user = userDao.getUserByEmailAndPassword(email, hash)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isEmailTaken(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }

    suspend fun updateUserProfile(
        email: String,
        fullName: String,
        phoneNumber: String,
        gender: String,
        dateOfBirth: String
    ): Result<User> {
        return try {
            val user = userDao.getUserByEmail(email)
                ?: return Result.failure(Exception("User not found"))
            val updated = user.copy(
                fullName = fullName,
                phoneNumber = phoneNumber,
                gender = gender,
                dateOfBirth = dateOfBirth,
                profileCompleted = true
            )
            userDao.updateUser(updated)
            Result.success(updated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String, newPassword: String): Result<Unit> {
        return try {
            val user = userDao.getUserByEmail(email)
                ?: return Result.failure(Exception("User not found"))
            val updated = user.copy(passwordHash = hashPassword(newPassword))
            userDao.updateUser(updated)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
