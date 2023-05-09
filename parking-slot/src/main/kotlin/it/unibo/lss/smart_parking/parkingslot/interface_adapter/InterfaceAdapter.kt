package it.unibo.lss.smart_parking.parkingslot.interface_adapter

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.http.*
import it.unibo.lss.smart_parking.parkingslot.entity.Center
import it.unibo.lss.smart_parking.parkingslot.entity.ParkingSlot
import it.unibo.lss.smart_parking.parkingslot.entity.Position
import it.unibo.lss.smart_parking.parkingslot.use_cases.UseCases
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.bson.Document
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import org.bson.types.ObjectId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

/*
Copyright (c) 2022-2023 Antonio Iannotta & Luca Bracchi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
data class InterfaceAdapter(val collection: MongoCollection<Document>): UseCases {
    override fun occupyParkingSlot(userId: String, slotId: String, stopEnd: Instant): Pair<HttpStatusCode, JsonObject> {
        val parkingSlot = getParkingSlot(slotId)

        return when {
            parkingSlot == null -> {
                createResponse(HttpStatusCode.NotFound, "errorCode", "ParkingSlotNotFound")
            }
            parkingSlot.occupied -> {
                createResponse(HttpStatusCode.BadRequest, "errorCode", "ParkingSlotOccupied")
            }
            !this.isTimeValid(stopEnd, Clock.System.now()) -> {
                createResponse(HttpStatusCode.BadRequest, "errorCode", "InvalidParkingSlotStopEnd")
            }
            else -> {
                val filter = Filters.eq("_id", ObjectId(slotId))
                val updates = Updates.combine(
                    Updates.set("occupied", true),
                    Updates.set("stopEnd", stopEnd.toJavaInstant()),
                    Updates.set("occupierId", ObjectId(userId))
                )

                collection.updateOne(filter, updates)

                createResponse(HttpStatusCode.OK, "successCode", "Success")
            }
        }
    }

    override fun incrementParkingSlotOccupation(userId: String, slotId: String, stopEnd: Instant): Pair<HttpStatusCode, JsonObject> {
        val parkingSlot = getParkingSlot(slotId)

        return when {
            parkingSlot == null -> {
                createResponse(HttpStatusCode.NotFound, "errorCode", "ParkingSlotNotFound")
            }
            !parkingSlot.occupied || parkingSlot.stopEnd == null -> {
                createResponse(HttpStatusCode.BadRequest, "errorCode", "ParkingSlotFree")
            }
            !this.isTimeValid(stopEnd, parkingSlot.stopEnd) -> {
                createResponse(HttpStatusCode.BadRequest, "errorCode", "InvalidParkingSlotStopEnd")
            }
            else -> {
                val filter = Filters.and(
                    Filters.eq("_id", ObjectId(slotId)),
                    Filters.eq("occupierId", ObjectId(userId))
                )
                val update = Updates.set("stopEnd", stopEnd.toJavaInstant())

                collection.updateOne(filter, update)

                createResponse(HttpStatusCode.OK, "successCode", "Success")
            }
        }
    }

    override fun freeParkingSlot(slotId: String): Pair<HttpStatusCode, JsonObject> {
        val parkingSlot = getParkingSlot(slotId)

        val freeResult: Pair<HttpStatusCode, JsonObject>

        if (parkingSlot == null) {
            freeResult = createResponse(HttpStatusCode.NotFound, "errorCode", "ParkingSlotNotFound")
        } else if (!parkingSlot.occupied) {
            // Nothing to do, parking slot is already free
            freeResult = createResponse(HttpStatusCode.OK, "successCode", "Success")
        } else {
            val filter = Filters.eq("_id", ObjectId(slotId))
            val updates = Updates.combine(
                Updates.set("occupied", false),
                Updates.unset("stopEnd"),
                Updates.unset("occupierId")
            )

            collection.updateOne(filter, updates)

            freeResult = createResponse(HttpStatusCode.OK, "successCode", "Success")
        }
        return freeResult
    }

    override fun getAllParkingSlotsByRadius(center: Center): List<ParkingSlot> {

        return collection
            .find(Filters.geoWithinCenterSphere("location", center.position.longitude, center.position.latitude, center.radius / 6371))
            .map {
                    document -> createParkingSlotFromDocument(document)
            }.toList()
    }

    override fun getParkingSlotList(): List<ParkingSlot> =
        collection.find().map {
                document -> createParkingSlotFromDocument(document)
        }.toList()

    override fun getParkingSlot(id: String): ParkingSlot? {
        val filter = Filters.eq("_id", ObjectId(id))
        val dbParkingSlot = collection.find(filter).first()

        val parkingSlot: ParkingSlot? = if (dbParkingSlot != null) {
            createParkingSlotFromDocument(dbParkingSlot)
        } else {
            null
        }
        return parkingSlot
    }

    override fun getParkingSlotOccupiedByUser(userId: String): ParkingSlot? {
        val filter = Filters.eq("occupierId", ObjectId(userId))

        val parkingSlotDocument = collection.find(filter).first()

        val parkingSlot: ParkingSlot? = if (parkingSlotDocument != null) {
            createParkingSlotFromDocument(parkingSlotDocument)
        } else {
            null
        }
        return parkingSlot
    }

    private fun createResponse(httpStatusCode: HttpStatusCode, code: String, message: String): Pair<HttpStatusCode, JsonObject> {
        val jsonObject = JsonObject(
            mapOf(
                code to Json.parseToJsonElement(message)
            )
        )

        return Pair(httpStatusCode, jsonObject)
    }

    private fun createParkingSlotFromDocument(document: Document): ParkingSlot {
        val coordinates = document
            .get("location", Document::class.java)
            .getList("coordinates", Number::class.java)
        val position = Position(
            longitude = coordinates[0].toDouble(),
            latitude = coordinates[1].toDouble()
        )
        return ParkingSlot(
            document.getObjectId("_id").toString(),
            document.getBoolean("occupied"),
            document.getDate("stopEnd")?.toKotlinInstant(),
            position,
            document.getObjectId("occupierId")?.toString()
        )
    }

    private fun Date.toKotlinInstant(): Instant =
        Instant.parse(DateTimeFormatter.ISO_DATE_TIME.format(toInstant().atOffset(ZoneOffset.UTC).toZonedDateTime()))
}
