package com.example.proscan.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.server.application.*
import java.util.*

object JwtConfig {
    private lateinit var secret: String
    private lateinit var issuer: String
    private lateinit var audience: String
    lateinit var realm: String
        private set
    private var accessTokenExpiryMs: Long = 3600000
    private var refreshTokenExpiryMs: Long = 604800000

    fun init(environment: ApplicationEnvironment) {
        secret = environment.config.property("jwt.secret").getString()
        issuer = environment.config.property("jwt.issuer").getString()
        audience = environment.config.property("jwt.audience").getString()
        realm = environment.config.property("jwt.realm").getString()
        accessTokenExpiryMs = environment.config.property("jwt.accessTokenExpiryMs").getString().toLong()
        refreshTokenExpiryMs = environment.config.property("jwt.refreshTokenExpiryMs").getString().toLong()
    }

    val verifier: JWTVerifier
        get() = JWT.require(Algorithm.HMAC256(secret))
            .withIssuer(issuer)
            .withAudience(audience)
            .build()

    fun generateAccessToken(userId: Long, email: String): String = JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("userId", userId)
        .withClaim("email", email)
        .withClaim("type", "access")
        .withExpiresAt(Date(System.currentTimeMillis() + accessTokenExpiryMs))
        .sign(Algorithm.HMAC256(secret))

    fun generateRefreshToken(userId: Long): String = JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("userId", userId)
        .withClaim("type", "refresh")
        .withExpiresAt(Date(System.currentTimeMillis() + refreshTokenExpiryMs))
        .sign(Algorithm.HMAC256(secret))
}
