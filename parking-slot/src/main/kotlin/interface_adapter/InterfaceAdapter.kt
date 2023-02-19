package interface_adapter


import entity.ParkingSlot
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import use_cases.IncrementOccupation
import use_cases.ParkingSlotUseCases
import use_cases.SlotId
import use_cases.SlotOccupation

object InterfaceAdapter {

    fun occupy(slotOccupation: SlotOccupation): Pair<HttpStatusCode, JsonObject> {
        val collection = "parking-slot"
        lateinit var response: Pair<HttpStatusCode, JsonObject>
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(collection)
        val returnValue = ParkingSlotUseCases.occupySlot(collection, slotOccupation, parkingSlotList)
        if (!returnValue) {
            val statusCode = HttpStatusCode.BadRequest
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
            response = Pair(statusCode, JsonObject(jsonElement))
        } else {
            val statusCode = HttpStatusCode.OK
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["successCode"] = Json.parseToJsonElement("Success")
            response = Pair(statusCode, JsonObject(jsonElement))
        }
        return response
    }

    fun incrementOccupation(incrementOccupation: IncrementOccupation): Pair<HttpStatusCode, JsonObject> {
        val collection = "parking-slot"
        lateinit var response: Pair<HttpStatusCode, JsonObject>
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(collection)
        val returnValue = ParkingSlotUseCases.incrementOccupation(collection, incrementOccupation, parkingSlotList)
        if (!returnValue) {
            val statusCode = HttpStatusCode.BadRequest
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
            response = Pair(statusCode, JsonObject(jsonElement))
        } else {
            val statusCode = HttpStatusCode.OK
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["successCode"] = Json.parseToJsonElement("Success")
            response = Pair(statusCode, JsonObject(jsonElement))
        }
        return response
    }

    fun free(slotId: SlotId): Pair<HttpStatusCode, JsonObject> {
        val collection = "parking-slot"
        lateinit var response: Pair<HttpStatusCode, JsonObject>
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(collection)
        val returnValue = ParkingSlotUseCases.freeSlot(collection, slotId, parkingSlotList)
        if (!returnValue) {
            val statusCode = HttpStatusCode.BadRequest
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
            response = Pair(statusCode, JsonObject(jsonElement))
        } else {
            val statusCode = HttpStatusCode.OK
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["successCode"] = Json.parseToJsonElement("Success")
            response = Pair(statusCode, JsonObject(jsonElement))
        }
        return response
    }

    fun getParkingSlotList(): List<ParkingSlot> {
        val collection = "parking-slot"
        return ParkingSlotUseCases.getAllParkingSlots(collection)
    }

    fun getParkingSlot(slotId: SlotId): Any {
        val collection = "parking-slot"
        lateinit var  response: Any
        val parkingSlot = ParkingSlotUseCases.getParkingSlot(collection, slotId.slotId)
        if (parkingSlot.id == "") {
            val statusCode = HttpStatusCode.BadRequest
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
            response = Pair(statusCode, JsonObject(jsonElement))
        } else {
            response = parkingSlot
        }
        return response
    }
}
