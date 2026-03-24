package com.example.proscan.plugins

import com.example.proscan.routes.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            // Public routes
            authRoutes()
            configRoutes()
            subscriptionRoutes()

            // Protected routes
            authenticate("jwt-auth") {
                documentRoutes()
                qrRoutes()
                analyticsRoutes()
                enterpriseRoutes()
                integrationRoutes()
                paymentRoutes()
                pdfToolRoutes()
            }
        }
    }
}
