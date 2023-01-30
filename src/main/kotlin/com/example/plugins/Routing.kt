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
        get("/parking-slot/occupy") {  } //put --> passare un oggetto che contiene ID e tempo di fine
        //tempo di fine: LocalDateTime convertito a String --> Questo oggetto si chiama "SlotOccupation"
        get("/parking-slot/increment-occupation") {  } //put -> passare un oggetto che contiene ID e nuovo tempo di
        //fine sosta. L'oggetto passato deve essere IncrementOccupation e avere come parametri lo slotId e il nuovo
        //tempo in LocalDateTime convertito a String
        get("/parking-slot/{parkingSlotId?}/free") {  } //put --> passare un oggetto Slot che contiene l'ID dello
        //slot da liberare
        get("/parking-slot/") {  }
        get("/parking-slot/{id?}") {  }
    }
}
