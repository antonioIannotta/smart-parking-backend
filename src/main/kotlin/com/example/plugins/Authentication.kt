package com.example.plugins

import com.example.user.controller.UserController
import com.example.user.model.response.ServerResponseBody
import com.example.user.routing.exposedUserRoutes
import com.example.user.routing.protectedUserRoutes
import com.example.user.utils.verifyJWT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureAuthentication() {

    val tokenSecret = environment.config.property("jwt.secret").getString()
    val mongoAddress = environment.config.property("mongo.address").getString()
    val databaseName = environment.config.property("mongo.database.name").getString()
    val collectionName = environment.config.property("mongo.database.collections.user").getString()
    val userController = UserController(mongoAddress, databaseName, collectionName, tokenSecret)

    install(Authentication) {

        jwt("auth-jwt") {
            realm = "Parking System Backend"
            verifier(verifyJWT(tokenSecret))
            validate { credential ->
                if (credential.payload.getClaim("email").asString() != "")
                    JWTPrincipal(credential.payload)
                else
                    null
            }
            challenge { _, _ ->
                call.response.status(HttpStatusCode.Unauthorized)
                call.respond(ServerResponseBody("unauthorizedUser", "Token is not valid or has expired"))
            }
        }

    }

    routing {
        authenticate("auth-jwt") {
            protectedUserRoutes(userController)
        }
        exposedUserRoutes(userController, tokenSecret)
    }

}