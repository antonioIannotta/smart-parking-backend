package it.unibo.lss.parking_system.entity.interface_adapter.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

/**
 * generates a jwt encrypted with HMAC256, valid for 2 hours
 * email: information to encrypt in the token
 * tokenSecret: encryption key of the jwt
 * duration: time required to jwt expiration, default is 7_200_000 (2 hours)
 */
fun generateJWT(email: String, tokenSecret: String, duration: Int = 7_200_000): String {
    val expirationDate = Date(System.currentTimeMillis() + duration)
    return JWT.create()
        .withAudience("Parking Client")
        .withClaim("email", email)
        .withExpiresAt(expirationDate)
        .sign(Algorithm.HMAC256(tokenSecret))
}

fun getJWTVerifier(tokenSecret: String): JWTVerifier = JWT.require(Algorithm.HMAC256(tokenSecret))
    .build()