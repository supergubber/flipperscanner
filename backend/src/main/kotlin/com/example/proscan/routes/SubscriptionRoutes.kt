package com.example.proscan.routes

import com.example.proscan.models.requests.UpgradeRequest
import com.example.proscan.plugins.userId
import com.example.proscan.service.SubscriptionService
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.subscriptionRoutes() {
    route("/subscriptions") {
        get("/plans") {
            val plans = SubscriptionService.getPlans()
            call.respond(plans)
        }

        authenticate("jwt-auth") {
            get("/current") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val sub = SubscriptionService.getCurrentSubscription(userId)
                call.respond(sub)
            }

            put("/upgrade") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val request = call.receive<UpgradeRequest>()
                require(request.plan in listOf("free", "pro", "enterprise")) {
                    "Invalid plan. Must be: free, pro, or enterprise"
                }
                val result = SubscriptionService.upgrade(userId, request.plan)
                call.respond(result)
            }

            get("/usage") {
                val userId = call.principal<JWTPrincipal>()!!.userId()
                val usage = SubscriptionService.getUsage(userId)
                call.respond(usage)
            }
        }
    }
}
