package it.unibo.lss.smart_parking.interface_adapter.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

/*
Copyright (c) 2022-2023 Antonio Iannotta & Luca Bracchi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
/**
 * generates a jwt encrypted with HMAC256, valid for 2 hours
 * email: information to encrypt in the token
 * tokenSecret: encryption key of the jwt
 * duration: time required to jwt expiration, default is 7_200_000 (2 hours)
 */
fun generateJWT(userId: String, tokenSecret: String, duration: Int = 7_200_000): String {
    val expirationDate = Date(System.currentTimeMillis() + duration)
    return JWT.create()
        .withAudience("Parking Client")
        .withClaim("userId", userId)
        .withExpiresAt(expirationDate)
        .sign(Algorithm.HMAC256(tokenSecret))
}

fun getJWTVerifier(tokenSecret: String): JWTVerifier = JWT.require(Algorithm.HMAC256(tokenSecret))
    .build()