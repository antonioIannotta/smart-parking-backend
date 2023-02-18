package com.example.framework.plugins

import com.example.framework.routing.exposedUserRoutes
import com.example.framework.routing.protectedUserRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(tokenSecret: String) {

    routing {

        get("/") {
            call.respondText("Hello World!")
        }
        get("/parking-slot/{parking-slotId?}/occupy") { }
        get("/parking-slot/{parkingSlotId?}/increment-occupation") { }
        get("/parking-slot/{parkingSlotId?}/free") { }
        get("/parking-slot/") { }
        get("/parking-slot/{id?}") { }

        authenticate("auth-jwt") {
            protectedUserRoutes()
        }
        exposedUserRoutes(tokenSecret)
    }
}
