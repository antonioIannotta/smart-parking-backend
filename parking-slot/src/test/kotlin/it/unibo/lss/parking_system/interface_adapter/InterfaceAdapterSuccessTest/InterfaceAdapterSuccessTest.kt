package it.unibo.lss.parking_system.interface_adapter.InterfaceAdapterSuccessTest

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import io.ktor.http.*
import it.unibo.lss.parking_system.interface_adapter.InterfaceAdapter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import java.time.Instant
import org.junit.jupiter.api.Test
import java.time.temporal.TemporalAmount
import kotlin.test.assertEquals

class InterfaceAdapterSuccessTest {

    @Test
    fun interfaceAdapterSuccessTest() {
        val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
        val databaseName = "ParkingSystem"
        val collectionName = "parking-slot-test"

        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)

        val interfaceAdapter = InterfaceAdapter(collection)
        var parkingSlotList = interfaceAdapter.getParkingSlotList()
        val occupyResult = interfaceAdapter.occupySlot("antonio", "A1", Instant.now().toString(), parkingSlotList)

        val jsonElementExpected = mutableMapOf<String, JsonElement>()
        jsonElementExpected["successCode"] = Json.parseToJsonElement("Success")
        val jsonObjectExpected = JsonObject(jsonElementExpected)

        assertEquals(Pair(HttpStatusCode.OK, jsonObjectExpected), occupyResult)

        Thread.sleep(60000)

        parkingSlotList = interfaceAdapter.getParkingSlotList()
        val incrementResult = interfaceAdapter.incrementOccupation("antonio", "A1", Instant.now().toString(), parkingSlotList)

        assertEquals(Pair(HttpStatusCode.OK, jsonObjectExpected), incrementResult)

        Thread.sleep(60000)
        parkingSlotList = interfaceAdapter.getParkingSlotList()
        val freeResult = interfaceAdapter.freeSlot("A1", parkingSlotList)

        assertEquals(Pair(HttpStatusCode.OK, jsonObjectExpected), freeResult)
    }
}