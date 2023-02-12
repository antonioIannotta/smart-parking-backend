package com.example.user.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

/**
 * generates a jwt encrypted with HMAC256, valid for 2 hours
 */
fun generateJWT(email: String, tokenSecret: String): String {
    val expirationDate = Date(System.currentTimeMillis() + 7_200_000) //valid for 2 hours
    return JWT.create()
        .withAudience("Parking Client")
        .withIssuer("luca bracchi")
        .withClaim("email", email)
        .withExpiresAt(expirationDate)
        .sign(Algorithm.HMAC256(tokenSecret))
}

fun verifyJWT(tokenSecret: String): JWTVerifier = JWT.require(Algorithm.HMAC256(tokenSecret))
    .build()