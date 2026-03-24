package com.example.proscan.routes

import com.example.proscan.models.requests.AddCardRequest
import com.example.proscan.models.requests.CreatePaymentMethodRequest
import com.example.proscan.models.responses.MessageResponse
import com.example.proscan.plugins.userId
import com.example.proscan.service.PaymentService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.paymentRoutes() {
    route("/payments") {
        route("/methods") {
            get {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val methods = PaymentService.getMethods(userId)
                call.respond(methods)
            }

            post {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val request = call.receive<CreatePaymentMethodRequest>()
                val method = PaymentService.addMethod(userId, request.name, request.icon, request.lastFour)
                call.respond(HttpStatusCode.Created, method)
            }

            post("/add-card") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val request = call.receive<AddCardRequest>()
                val lastFour = request.cardNumber.takeLast(4)
                val method = PaymentService.addMethod(userId, "Credit Card", "CreditCard", lastFour)
                call.respond(HttpStatusCode.Created, method)
            }

            put("/{id}/default") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val methodId = call.parameters["id"]?.toLongOrNull()
                    ?: throw IllegalArgumentException("Invalid payment method ID")
                val result = PaymentService.setDefault(userId, methodId)
                call.respond(result)
            }

            delete("/{id}") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val methodId = call.parameters["id"]?.toLongOrNull()
                    ?: throw IllegalArgumentException("Invalid payment method ID")
                PaymentService.deleteMethod(userId, methodId)
                call.respond(MessageResponse(message = "Payment method removed"))
            }
        }

        get("/history") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val history = PaymentService.getHistory(userId)
            call.respond(history)
        }
    }
}
