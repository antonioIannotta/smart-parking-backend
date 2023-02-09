package com.example.plugins

import com.example.user.routing.exposedUserRoutes
import com.example.user.routing.protectedUserRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureAuthentication() {

    install(Authentication) {

        basic("auth-basic") {
            realm = "Access to the '/' path"
            validate { credentials ->
                if (credentials.name == "luca.bracchi" && credentials.password == "test123") {
                    print("success validation")
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

    }

    routing {

        authenticate("auth-basic") {

            get("/auth-test") {
                call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
            }

            protectedUserRoutes()

        }

        exposedUserRoutes()

    }

}