package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/user/sign-up") {  }
        get("/user/sign-in") {  }
        get("/user/{userId?}/parking-slot") {  }
        get("/user/{userId?}/logout") {  }
        get("/user/{userId?}/delete") {  }
        get("/parking-slot/{parking-slotId?}/occupy") {  }
        get("/parking-slot/{parkingSlotId?}/increment-occupation") {  }
        get("/parking-slot/{parkingSlotId?}/free") {  }
        get("/parking-slot/") {  }
        get("/parking-slot/{id?}") {  }
    }
}
