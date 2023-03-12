package it.unibo.lss.smart_parking.interface_adapter

import com.mongodb.client.MongoClients
import io.ktor.http.*
import it.unibo.lss.smart_parking.FillParkingSlotCollection
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

class InterfaceAdapterSuccessTest {

    @Test
    fun interfaceAdapterSuccessTest() {
        val userId = ObjectId().toString()
        val slotId = FillParkingSlotCollection.eraseAndFillCollection("parking-slot-test", 1).first()


        val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
        val databaseName = "ParkingSystem"
        val collectionName = "parking-slot-test"

        val mongoClient = MongoClients.create(mongoAddress)
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)

        val interfaceAdapter = InterfaceAdapter(collection)
        val occupyResult = interfaceAdapter.occupySlot(userId, slotId, Clock.System.now().plus(1.hours))

        val jsonElementExpected = mapOf(
            "successCode" to Json.parseToJsonElement("Success")
        )
        val jsonObjectExpected = JsonObject(jsonElementExpected)

        assertEquals(Pair(HttpStatusCode.OK, jsonObjectExpected), occupyResult)

        Thread.sleep(60000)

        val incrementResult = interfaceAdapter.incrementOccupation(userId, slotId, Clock.System.now().plus(3.hours))

        assertEquals(Pair(HttpStatusCode.OK, jsonObjectExpected), incrementResult)

        Thread.sleep(60000)
        val freeResult = interfaceAdapter.freeSlot(slotId)

        assertEquals(Pair(HttpStatusCode.OK, jsonObjectExpected), freeResult)
    }
}