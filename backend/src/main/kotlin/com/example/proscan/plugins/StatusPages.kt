package com.example.proscan.plugins

import com.example.proscan.models.responses.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(
                error = "Bad Request",
                message = cause.message ?: "Invalid request"
            ))
        }
        exception<NoSuchElementException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ErrorResponse(
                error = "Not Found",
                message = cause.message ?: "Resource not found"
            ))
        }
        exception<Exception> { call, cause ->
            call.application.environment.log.error("Unhandled exception", cause)
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse(
                error = "Internal Server Error",
                message = cause.message ?: "An unexpected error occurred"
            ))
        }
    }
}
