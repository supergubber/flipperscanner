package com.example.proscan.routes

import com.example.proscan.models.requests.CreateQrRequest
import com.example.proscan.models.responses.MessageResponse
import com.example.proscan.plugins.userId
import com.example.proscan.service.QrService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.qrRoutes() {
    route("/qr") {
        post {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val request = call.receive<CreateQrRequest>()
            val qr = QrService.create(userId, request.type, request.content, request.label)
            call.respond(HttpStatusCode.Created, qr)
        }

        get {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val qrCodes = QrService.list(userId)
            call.respond(qrCodes)
        }

        get("/history") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val qrCodes = QrService.list(userId)
            call.respond(qrCodes)
        }

        get("/{id}") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val qrId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid QR code ID")
            val qr = QrService.getById(qrId, userId)
            call.respond(qr)
        }

        delete("/{id}") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val qrId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid QR code ID")
            QrService.delete(qrId, userId)
            call.respond(MessageResponse(message = "QR code deleted"))
        }
    }
}
