package com.example.plugins

import com.example.parkingSlot.database.Database
import com.example.parkingSlot.models.IncrementOccupation
import com.example.parkingSlot.models.SlotOccupation
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*

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
        put("/parking-slot/occupy") {
            val slotOccupation = call.receive<SlotOccupation>()
            Database.occupySlot(slotOccupation)
        }
        put("/parking-slot/increment-occupation") {
            val incrementOccupation = call.receive<IncrementOccupation>()
            Database.incrementOccupation(incrementOccupation)
        }
        get("/parking-slot/{parkingSlotId?}/free") {  } //put --> passare un oggetto Slot che contiene l'ID dello
        //slot da liberare
        get("/parking-slot/") {  }
        get("/parking-slot/{id?}") {  }
    }
}
