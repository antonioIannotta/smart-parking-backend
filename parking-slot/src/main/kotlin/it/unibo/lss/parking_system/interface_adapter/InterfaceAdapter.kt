package it.unibo.lss.parking_system.interface_adapter


import it.unibo.lss.parking_system.entity.ParkingSlot
import io.ktor.http.*
import it.unibo.lss.parking_system.entity.Center
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import it.unibo.lss.parking_system.use_cases.IncrementOccupation
import it.unibo.lss.parking_system.use_cases.ParkingSlotUseCases
import it.unibo.lss.parking_system.use_cases.SlotOccupation

object InterfaceAdapter {

    fun occupy(userId: String, slotId: String, stopEnd: String): Pair<HttpStatusCode, JsonObject> {
        val collection = "parking-slot"
        val parkingSlotList = ParkingSlotUseCases.getParkingSlotList(collection)
        return ParkingSlotUseCases.occupySlot(collection,userId, slotId, stopEnd, parkingSlotList)
    }

    fun incrementOccupation(userId: String, slotId: String, stopEnd: String): Pair<HttpStatusCode, JsonObject> {
        val collection = "parking-slot"
        val parkingSlotList = ParkingSlotUseCases.getParkingSlotList(collection)
        return ParkingSlotUseCases.incrementOccupation(collection, userId, slotId, stopEnd, parkingSlotList)
    }

    fun free(slotId: String): Pair<HttpStatusCode, JsonObject> {
        val collection = "parking-slot"
        val parkingSlotList = ParkingSlotUseCases.getParkingSlotList(collection)
        return ParkingSlotUseCases.freeSlot(collection, slotId, parkingSlotList)
    }

    fun getParkingSlotList(center: Center): List<ParkingSlot> {
        val collection = "parking-slot"
        return ParkingSlotUseCases.getAllParkingSlotsByRadius(collection, center)
    }

    fun getParkingSlot(slotId: String): Any {
        val collection = "parking-slot"
        lateinit var  response: Any
        val parkingSlot = ParkingSlotUseCases.getParkingSlot(collection, slotId)
        if (parkingSlot.id == "") {
            val statusCode = HttpStatusCode.NotFound
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotNotFound")
            response = Pair(statusCode, JsonObject(jsonElement))
        } else {
            response = parkingSlot
        }
        return response
    }
}
