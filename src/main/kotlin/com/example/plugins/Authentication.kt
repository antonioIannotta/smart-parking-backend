package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.user.routing.exposedUserRoutes
import com.example.user.routing.protectedUserRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureAuthentication() {

    val tokenSecret = "dSgUkXp2s5v8y/B?E(H+MbQeThWmYq3t"

    install(Authentication) {

        jwt("auth-jwt") {

            realm = "Parking System Backend"

            verifier(JWT
                .require(Algorithm.HMAC256(tokenSecret))
                .build()
            )

            validate { credential ->
                if(credential.payload.getClaim("email").asString() != "")
                    JWTPrincipal(credential.payload)
                else
                    null
            }

        }

    }

    routing {
        authenticate("auth-jwt") {
            protectedUserRoutes()
        }
        exposedUserRoutes(tokenSecret)
    }

}