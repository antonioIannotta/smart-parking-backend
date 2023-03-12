package it.unibo.lss.smart_parking.interface_adapter

import com.mongodb.client.MongoClients
import io.ktor.http.*
import it.unibo.lss.smart_parking.FillParkingSlotCollection
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InterfaceAdapterSuccessTest {

    @Test
    fun interfaceAdapterSuccessTest() {

        FillParkingSlotCollection.eraseAndFillCollection("parking-slot-test", 10)


        val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
        val databaseName = "ParkingSystem"
        val collectionName = "parking-slot-test"

        val mongoClient = MongoClients.create(mongoAddress)
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)

        val interfaceAdapter = InterfaceAdapter(collection)
        val occupyResult = interfaceAdapter.occupySlot("antonio", "A1", Clock.System.now())

        val jsonElementExpected = mutableMapOf<String, JsonElement>()
        jsonElementExpected["successCode"] = Json.parseToJsonElement("Success")
        val jsonObjectExpected = JsonObject(jsonElementExpected)

        assertEquals(Pair(HttpStatusCode.OK, jsonObjectExpected), occupyResult)

        Thread.sleep(60000)

        val incrementResult = interfaceAdapter.incrementOccupation("antonio", "A1", Clock.System.now())

        assertEquals(Pair(HttpStatusCode.OK, jsonObjectExpected), incrementResult)

        Thread.sleep(60000)
        val freeResult = interfaceAdapter.freeSlot("A1")

        assertEquals(Pair(HttpStatusCode.OK, jsonObjectExpected), freeResult)
    }
}