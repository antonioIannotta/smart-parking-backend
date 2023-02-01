package com.example.plugins

import com.example.parkingSlot.database.Database
import com.example.parkingSlot.models.IncrementOccupation
import com.example.parkingSlot.models.SlotId
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
            val parkingSlotList = Database.getAllParkingSlots()
            val returnValue = Database.occupySlot(slotOccupation, parkingSlotList)
            call.respond(returnValue)
        }
        put("/parking-slot/increment-occupation") {
            val incrementOccupation = call.receive<IncrementOccupation>()
            Database.incrementOccupation(incrementOccupation)
        }
        put("/parking-slot/free") {
            val slotId = call.receive<SlotId>()
            Database.freeSlot(slotId)
        }
        get("/parking-slot/") {
            val parkingSlotList = Database.getAllParkingSlots()
            call.respond(parkingSlotList)
        }
        get("/parking-slot/{id?}") {
            val parkingSlot = Database.getParkingSlot(call.parameters["id"]!!)
            call.respond(parkingSlot)
        }
    }
}
