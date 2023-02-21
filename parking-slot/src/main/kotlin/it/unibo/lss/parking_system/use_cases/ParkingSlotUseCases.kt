package it.unibo.lss.parking_system.use_cases

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.client.model.geojson.Point
import io.ktor.http.*
import it.unibo.lss.parking_system.entity.Center
import it.unibo.lss.parking_system.entity.ParkingSlot
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.bson.Document
import org.bson.conversions.Bson
import java.time.Instant
import java.time.LocalDateTime

/**
 * This object represents the operation that are performed on the database as response of a certain requests by the client
 */
object ParkingSlotUseCases {

    private const val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
    private const val databaseName = "ParkingSystem"

    /**
     * Function that occupy a certain slot based on the slotOccupation object received as argument
     */
    fun occupySlot(userId: String, slotId: String, stopEnd: String, parkingSlotList: MutableList<ParkingSlot>, collection: MongoCollection<Document>): Pair<HttpStatusCode, JsonObject> {

        lateinit var returnValue: Pair<HttpStatusCode, JsonObject>

        if (isSlotOccupied(slotId, parkingSlotList)) {
            val statusCode = HttpStatusCode.BadRequest
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotIsOccupied")
            returnValue = Pair(statusCode, JsonObject(jsonElement))
        } else if (!isParkingSlotValid(slotId, parkingSlotList)) {
            val statusCode = HttpStatusCode.NotFound
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
            returnValue = Pair(statusCode, JsonObject(jsonElement))
        } else {

            val filter = Filters.eq("id", slotId)
            val updates = mutableListOf<Bson>()
            updates.add(Updates.set("occupied", true))
            updates.add(Updates.set("endStop", stopEnd))
            updates.add(Updates.set("userId", userId))
            val options = UpdateOptions().upsert(true)

            collection.updateOne(filter, updates, options)

            val statusCode = HttpStatusCode.NotFound
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["successCode"] = Json.parseToJsonElement("Success")
            returnValue = Pair(statusCode, JsonObject(jsonElement))
        }

        return returnValue
    }

    /**
     * Function used to check the state of a certain parking slot
     */
    fun isSlotOccupied(slotId: String, parkingSlotList: MutableList<ParkingSlot>): Boolean =
        parkingSlotList.first { ps -> ps.id == slotId
        }.occupied

    /**
     * Function that increment a certain slot based on the incrementOccupation object received as argument
     */
    fun incrementOccupation(userId: String, slotId: String, stopEnd: String, parkingSlotList: MutableList<ParkingSlot>, collection: MongoCollection<Document>): Pair<HttpStatusCode, JsonObject> {

        lateinit var returnValue: Pair<HttpStatusCode, JsonObject>

        if (!isParkingSlotValid(slotId, parkingSlotList)) {
            val statusCode = HttpStatusCode.NotFound
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
            returnValue = Pair(statusCode, JsonObject(jsonElement))
        } else if(!isSlotOccupied(slotId, parkingSlotList)) {
            val statusCode = HttpStatusCode.BadRequest
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotNotOccupied")
            returnValue = Pair(statusCode, JsonObject(jsonElement))
        } else {
            val parkingSlot = getParkingSlot(collection, slotId)
            if (!isTimeValid(stopEnd, parkingSlot.stopEnd)) {
                val statusCode = HttpStatusCode.BadRequest
                val jsonElement = mutableMapOf<String, JsonElement>()
                jsonElement["errorCode"] = Json.parseToJsonElement("TimeIncrementNotValid")
                returnValue = Pair(statusCode, JsonObject(jsonElement))
            } else {
                val mongoClient = MongoClient(MongoClientURI(mongoAddress))

                val filters = mutableListOf<Bson>()
                filters.add(Filters.eq("slotId", slotId))
                filters.add(Filters.eq("userId", userId))
                val filter = Filters.and(filters)
                val update = Updates.set("endStop", Instant.parse(stopEnd).toString())

                collection.updateOne(filter, update)

                mongoClient.close()

                val statusCode = HttpStatusCode.OK
                val jsonElement = mutableMapOf<String, JsonElement>()
                jsonElement["successCode"] = Json.parseToJsonElement("Success")
                returnValue = Pair(statusCode, JsonObject(jsonElement))
            }
        }

        return returnValue
    }

    /**
     * Function used to check if the time inserted during the increment stop phase is greater than the actual one
     */
    fun isTimeValid(actualTime: String, previousTime: String) =
        Instant.parse(actualTime).isAfter(Instant.parse(previousTime))


    /**
     * Function that free a certain slot based on the slotId object received as argument
     */
    fun freeSlot(collection: MongoCollection<Document>, slotId: String, parkingSlotList: MutableList<ParkingSlot>): Pair<HttpStatusCode, JsonObject> {

        lateinit var returnValue: Pair<HttpStatusCode, JsonObject>

        if (!isParkingSlotValid(slotId, parkingSlotList)) {
            val statusCode = HttpStatusCode.NotFound
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotNotValid")
            returnValue = Pair(statusCode, JsonObject(jsonElement))
        }

        else if (isSlotOccupied(slotId, parkingSlotList)) {
            val mongoClient = MongoClient(MongoClientURI(mongoAddress))

            val filter = Filters.eq("id", slotId)
            val updates = emptyList<Bson>().toMutableList()
            updates.add(Updates.set("occupied", false))
            updates.add(Updates.set("endStop", ""))
            updates.add(Updates.set("userId", ""))

            val options = UpdateOptions().upsert(true)

            collection.updateOne(filter, updates, options)

            mongoClient.close()

            val statusCode = HttpStatusCode.OK
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["successCode"] = Json.parseToJsonElement("Success")
            returnValue = Pair(statusCode, JsonObject(jsonElement))

        } else {
            val statusCode = HttpStatusCode.NotFound
            val jsonElement = mutableMapOf<String, JsonElement>()
            jsonElement["errorCode"] = Json.parseToJsonElement("ParkingSlotNotOccupied")
            returnValue = Pair(statusCode, JsonObject(jsonElement))
        }

        return returnValue
    }

    /**
     * Function that returns all the parking slots that are stored into the database
     */
    fun getAllParkingSlotsByRadius(collection: MongoCollection<Document>, center: Center): MutableList<ParkingSlot> {

        val parkingSlotList = emptyList<ParkingSlot>().toMutableList()

        collection
            .find(Filters.geoWithinCenterSphere("location", center.position.longitude, center.position.latitude, center.radius / 6371))
            .forEach {
                document ->  parkingSlotList.add(createParkingSlotFromDocument(document))
            }
        return parkingSlotList
    }

    fun getParkingSlotList(collection: MongoCollection<Document>): MutableList<ParkingSlot> {
        val parkingSlotList = emptyList<ParkingSlot>().toMutableList()

        collection.find().forEach {
            document -> parkingSlotList.add(createParkingSlotFromDocument(document))
        }
        return parkingSlotList
    }

    /**
     * Function that returns the parking slot corresponding to the given Id
     */
    fun getParkingSlot(collection: MongoCollection<Document>, id: String): ParkingSlot {
        val parkingSlotList = mutableListOf<ParkingSlot>()

        collection.find().forEach {
                document -> parkingSlotList.add(createParkingSlotFromDocument(document))
        }

        var parkingSlot: ParkingSlot = if (isParkingSlotValid(id, parkingSlotList)) {
            parkingSlotList.first {
                    parkingSlot -> parkingSlot.id == id
            }
        } else {
            ParkingSlot("", false, "", 0.0, 0.0, "")
        }
        return parkingSlot
    }

    /**
     * Function that returns a parking slot given a certain Bson Document
     */
    fun createParkingSlotFromDocument(document: Document): ParkingSlot {
        return ParkingSlot(
            document["id"].toString(),
            document["occupied"].toString().toBoolean(),
            document["endStop"].toString(),
            (document["location"] as Point).position.values[1],
            (document["longitude"] as Point).position.values[0],
            document["userId"].toString()
        )
    }


    fun isParkingSlotValid(slotId: String, parkingSlotList: MutableList<ParkingSlot>) =
        parkingSlotList.filter {
                parkingSlot -> parkingSlot.id == slotId
        }.isNotEmpty()

}