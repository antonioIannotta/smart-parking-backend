package it.unibo.lss.smart_parking.framework.plugins

import it.unibo.lss.smart_parking.framework.routing.exposedUserRoutes
import it.unibo.lss.smart_parking.framework.routing.protectedUserRoutes
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureRouting(tokenSecret: String) {

    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        authenticate("auth-jwt") {
            protectedUserRoutes()
        }
        exposedUserRoutes(tokenSecret)

    }
}
