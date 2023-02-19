package com.example.plugins

import com.example.parkingSlot.use_cases.IncrementOccupation
import com.example.parkingSlot.use_cases.SlotId
import com.example.parkingSlot.use_cases.SlotOccupation
import com.example.parkingSlot.interface_adapter.InterfaceAdapter
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
            val response = InterfaceAdapter.occupy(slotOccupation)
            call.respond(response.first, response.second)
        }
        put("/parking-slot/increment-occupation") {

            val incrementOccupation = call.receive<IncrementOccupation>()
            val response = InterfaceAdapter.incrementOccupation(incrementOccupation)
            call.respond(response.first, response.second)
        }
        put("/parking-slot/free") {

            val slotId = call.receive<SlotId>()
            val response = InterfaceAdapter.free(slotId)
            call.respond(response.first, response.second)

        }
        get("/parking-slot/") {

            val response = InterfaceAdapter.getParkingSlotList()
            call.respond(response)
        }
        get("/parking-slot/") {

            val slotId = call.receive<SlotId>()
            val response = InterfaceAdapter.getParkingSlot(slotId)
            call.respond(response)
        }
    }
}
