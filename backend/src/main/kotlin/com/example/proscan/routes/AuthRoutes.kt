package com.example.proscan.routes

import com.example.proscan.models.requests.*
import com.example.proscan.models.responses.MessageResponse
import com.example.proscan.models.responses.TokenPairResponse
import com.example.proscan.plugins.userId
import com.example.proscan.service.AuthService
import com.example.proscan.util.JwtConfig
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
            require(request.email.contains("@")) { "Invalid email format" }
            require(request.password.length >= 6) { "Password must be at least 6 characters" }
            val result = AuthService.register(request.name, request.email, request.password)
            call.respond(HttpStatusCode.Created, result)
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val result = AuthService.login(request.email, request.password)
            call.respond(result)
        }

        post("/refresh") {
            val request = call.receive<RefreshRequest>()
            try {
                val decoded = JwtConfig.verifier.verify(request.refreshToken)
                val type = decoded.getClaim("type").asString()
                require(type == "refresh") { "Not a refresh token" }
                val userId = decoded.getClaim("userId").asLong()
                val (accessToken, refreshToken) = AuthService.refreshToken(userId)
                call.respond(TokenPairResponse(token = accessToken, refreshToken = refreshToken))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, MessageResponse(
                    success = false,
                    message = "Invalid refresh token"
                ))
            }
        }

        post("/forgot-password") {
            val request = call.receive<ForgotPasswordRequest>()
            call.respond(MessageResponse(message = "Password reset OTP sent to ${request.email}"))
        }

        post("/reset-password") {
            val request = call.receive<ResetPasswordRequest>()
            AuthService.resetPassword(request.email, request.otp, request.newPassword)
            call.respond(MessageResponse(message = "Password reset successful"))
        }

        authenticate("jwt-auth") {
            get("/profile") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val profile = AuthService.getProfile(userId)
                call.respond(profile)
            }

            put("/profile") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val request = call.receive<UpdateProfileRequest>()
                val updated = AuthService.updateProfile(userId, request)
                call.respond(updated)
            }

            post("/logout") {
                // Stateless JWT — client discards tokens. Server acknowledges.
                call.respond(MessageResponse(message = "Logged out successfully"))
            }

            delete("/account") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                AuthService.deleteAccount(userId)
                call.respond(MessageResponse(message = "Account deleted successfully"))
            }

            get("/stats") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val stats = AuthService.getUserStats(userId)
                call.respond(stats)
            }

            get("/preferences") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val prefs = AuthService.getPreferences(userId)
                call.respond(prefs)
            }

            put("/preferences") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val request = call.receive<UpdatePreferencesRequest>()
                val updated = AuthService.updatePreferences(userId, request)
                call.respond(updated)
            }
        }
    }
}
