package com.example.proscan.plugins

import com.example.proscan.models.responses.ErrorResponse
import com.example.proscan.util.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {
    JwtConfig.init(environment)

    install(Authentication) {
        jwt("jwt-auth") {
            realm = JwtConfig.realm
            verifier(JwtConfig.verifier)
            validate { credential ->
                val userId = credential.payload.getClaim("userId")?.asLong()
                val type = credential.payload.getClaim("type")?.asString()
                if (userId != null && type == "access") {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse(
                    error = "Unauthorized",
                    message = "Invalid or expired token"
                ))
            }
        }
    }
}

fun JWTPrincipal.userId(): Long =
    payload.getClaim("userId").asLong()
