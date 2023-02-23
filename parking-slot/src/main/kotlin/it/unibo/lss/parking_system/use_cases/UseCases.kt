package it.unibo.lss.parking_system.use_cases

import com.mongodb.client.MongoCollection
import io.ktor.http.*
import it.unibo.lss.parking_system.entity.Center
import it.unibo.lss.parking_system.entity.ParkingSlot
import kotlinx.serialization.json.JsonObject
import org.bson.Document
import java.time.Instant

interface UseCases {
    fun occupySlot(userId: String, slotId: String, stopEnd: String, parkingSlotList: MutableList<ParkingSlot>): Pair<HttpStatusCode, JsonObject>
    fun incrementOccupation(userId: String, slotId: String, stopEnd: String, parkingSlotList: MutableList<ParkingSlot>): Pair<HttpStatusCode, JsonObject>
    fun freeSlot(slotId: String, parkingSlotList: MutableList<ParkingSlot>): Pair<HttpStatusCode, JsonObject>
    fun getAllParkingSlotsByRadius(center: Center): MutableList<ParkingSlot>
    fun getParkingSlotList(): MutableList<ParkingSlot>
    fun getParkingSlot(id: String): Any

    fun isSlotOccupied(slotId: String, parkingSlotList: MutableList<ParkingSlot>): Boolean =
        parkingSlotList.first { ps -> ps.id == slotId
        }.occupied

    fun isTimeValid(actualTime: String, previousTime: String): Boolean {
        return Instant.parse(actualTime).isAfter(Instant.parse(previousTime))
    }

    fun isParkingSlotValid(slotId: String, parkingSlotList: MutableList<ParkingSlot>) =
        parkingSlotList.filter {
                parkingSlot -> parkingSlot.id == slotId
        }.isNotEmpty()

}