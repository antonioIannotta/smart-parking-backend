package com.example.plugins

import com.example.parkingSlot.database.Database
import com.example.parkingSlot.models.IncrementOccupation
import com.example.parkingSlot.models.SlotId
import com.example.parkingSlot.models.SlotOccupation
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

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

            val collection = "parking-slot"

            val slotOccupation = call.receive<SlotOccupation>()
            val parkingSlotList = Database.getAllParkingSlots(collection)
            val returnValue = Database.occupySlot(collection, slotOccupation, parkingSlotList)
            if (!returnValue) {
                val response = mutableMapOf<String, JsonElement>()
                response["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
                call.respond(HttpStatusCode.BadRequest, JsonObject(response))
            } else {
                val response = mutableMapOf<String, JsonElement>()
                response["success"] = Json.parseToJsonElement("ParkingSlotOccupied")
                call.respond(JsonObject(response))
            }
        }
        put("/parking-slot/increment-occupation") {

            val collection = "parking-slot"

            val incrementOccupation = call.receive<IncrementOccupation>()
            val parkingSlotList = Database.getAllParkingSlots(collection)
            val returnValue = Database.incrementOccupation(collection, incrementOccupation, parkingSlotList)
            if (!returnValue) {
                val response = mutableMapOf<String, JsonElement>()
                response["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
                call.respond(HttpStatusCode.BadRequest, JsonObject(response))
            } else {
                val response = mutableMapOf<String, JsonElement>()
                response["success"] = Json.parseToJsonElement("EndStopIncremented")
                call.respond(JsonObject(response))
            }
        }
        put("/parking-slot/free") {

            val collection = "parking-slot"

            val slotId = call.receive<SlotId>()
            val parkingSlotList = Database.getAllParkingSlots(collection)
            val returnValue = Database.freeSlot(collection, slotId, parkingSlotList)
            if (!returnValue) {
                val response = mutableMapOf<String, JsonElement>()
                response["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
                call.respond(HttpStatusCode.BadRequest, JsonObject(response))
            } else {
                val response = mutableMapOf<String, JsonElement>()
                response["success"] = Json.parseToJsonElement("ParkingSlotIsFree")
                call.respond(JsonObject(response))
            }

        }
        get("/parking-slot/") {

            val collection = "parking-slot"

            val parkingSlotList = Database.getAllParkingSlots(collection)
            call.respond(parkingSlotList)
        }
        get("/parking-slot/") {

            val collection = "parking-slot"

            val slotId = call.receive<SlotId>()
            val parkingSlot = Database.getParkingSlot(collection, slotId.slotId)
            if (parkingSlot.id == "") {
                val response = mutableMapOf<String, JsonElement>()
                response["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
                call.respond(HttpStatusCode.BadRequest, JsonObject(response))
            } else {
                call.respond(parkingSlot)
            }
        }
    }
}
