package com.example.proscan.routes

import com.example.proscan.plugins.userId
import com.example.proscan.service.AnalyticsService
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.analyticsRoutes() {
    route("/analytics") {
        get("/dashboard") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val dashboard = AnalyticsService.getDashboard(userId)
            call.respond(dashboard)
        }

        get("/scan-stats") {
            val userId = call.principal<JWTPrincipal>()!!.userId()
            val period = call.request.queryParameters["period"] ?: "week"
            val stats = AnalyticsService.getScanStats(userId, period)
            call.respond(stats)
        }
    }
}
