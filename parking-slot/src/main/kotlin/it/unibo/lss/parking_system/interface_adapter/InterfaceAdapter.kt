package it.unibo.lss.parking_system.interface_adapter


import com.mongodb.client.MongoCollection
import it.unibo.lss.parking_system.entity.ParkingSlot
import io.ktor.http.*
import it.unibo.lss.parking_system.entity.Center
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import it.unibo.lss.parking_system.use_cases.IncrementOccupation
import it.unibo.lss.parking_system.use_cases.ParkingSlotUseCases
import it.unibo.lss.parking_system.use_cases.SlotOccupation
import org.bson.Document

object InterfaceAdapter {

    fun occupy(userId: String, slotId: String, stopEnd: String, collection: MongoCollection<Document>): Pair<HttpStatusCode, JsonObject> {
        val parkingSlotList = ParkingSlotUseCases.getParkingSlotList(collection)
        return ParkingSlotUseCases.occupySlot(userId, slotId, stopEnd, parkingSlotList, collection)
    }

    fun incrementOccupation(userId: String, slotId: String, stopEnd: String, collection: MongoCollection<Document>): Pair<HttpStatusCode, JsonObject> {
        val parkingSlotList = ParkingSlotUseCases.getParkingSlotList(collection)
        return ParkingSlotUseCases.incrementOccupation(userId, slotId, stopEnd, parkingSlotList, collection)
    }

    fun free(slotId: String, collection: MongoCollection<Document>): Pair<HttpStatusCode, JsonObject> {
        val parkingSlotList = ParkingSlotUseCases.getParkingSlotList(collection)
        return ParkingSlotUseCases.freeSlot(collection, slotId, parkingSlotList)
    }

    fun getParkingSlotList(center: Center, collection: MongoCollection<Document>): List<ParkingSlot> {
        return ParkingSlotUseCases.getAllParkingSlotsByRadius(collection, center)
    }

    fun getParkingSlot(slotId: String, collection: MongoCollection<Document>): Any {
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
