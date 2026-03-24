package com.example.proscan.routes

import com.example.proscan.models.requests.IntegrationConfigRequest
import com.example.proscan.plugins.userId
import com.example.proscan.service.IntegrationService
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.integrationRoutes() {
    route("/integrations") {
        get {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val integrations = IntegrationService.getAll(userId)
            call.respond(integrations)
        }

        post("/{id}/connect") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val integrationId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid integration ID")
            val result = IntegrationService.connect(userId, integrationId)
            call.respond(result)
        }

        post("/{id}/disconnect") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val integrationId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid integration ID")
            val result = IntegrationService.disconnect(userId, integrationId)
            call.respond(result)
        }

        get("/{id}/config") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val integrationId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid integration ID")
            val config = IntegrationService.getConfig(userId, integrationId)
            call.respond(config)
        }

        post("/{id}/configure") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val integrationId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid integration ID")
            val request = call.receive<IntegrationConfigRequest>()
            val result = IntegrationService.saveConfig(userId, integrationId, request.apiKey, request.webhookUrl)
            call.respond(result)
        }
    }
}
