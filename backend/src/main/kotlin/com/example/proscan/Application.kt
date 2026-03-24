package com.example.proscan

import com.example.proscan.database.DatabaseFactory
import com.example.proscan.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init(environment.config)
    configureSerialization()
    configureAuthentication()
    configureCORS()
    configureStatusPages()
    configureRouting()

    log.info("ProScan Backend started")
    log.info("API base: http://localhost:8080/api")
    log.info("Config endpoint: http://localhost:8080/api/config")
}
