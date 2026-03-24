package com.example.proscan.routes

import com.example.proscan.service.ConfigService
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configRoutes() {
    get("/config") {
        val config = ConfigService.getAppConfig()
        call.respond(config)
    }
}
