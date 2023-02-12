package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/parking-slot/{parking-slotId?}/occupy") { }
        get("/parking-slot/{parkingSlotId?}/increment-occupation") { }
        get("/parking-slot/{parkingSlotId?}/free") { }
        get("/parking-slot/") { }
        get("/parking-slot/{id?}") { }

//        userRoutes()
    }
}
