package com.example.database

import com.example.parkingSlot.database.Database
import com.example.parkingSlot.models.ParkingSlot
import io.ktor.server.http.content.*
import kotlin.test.Test
import java.time.LocalDateTime
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.bson.Document

class DatabaseTest {

    private var parkingSlotList =
        mutableListOf<ParkingSlot>(ParkingSlot("A1", false, ""),
            ParkingSlot("B2", false, ""),
            ParkingSlot("C3", true, LocalDateTime.now().toString()))

    @Test
    fun createParkingSlotFromDocumentTest() {
        val parkingSlotDocument = Document().append("id", "A9").append("occupied", "false").append("endStop", "")
        val parkingSlot = ParkingSlot("A9", false, "")
        val parkingSlotFromDocument = Database.createParkingSlotFromDocument(parkingSlotDocument)

        assertEquals(parkingSlot.id, parkingSlotFromDocument.id)
        assertEquals(parkingSlot.occupied, parkingSlotFromDocument.occupied)
        assertEquals(parkingSlot.endStop, parkingSlotFromDocument.endStop)
    }

    @Test
    fun isOccupiedSlotTest() {
        val slotId1 = "B2"
        assertEquals(false, Database.isSlotOccupied(slotId1, parkingSlotList))

        val slotId2 = "C3"
        assertEquals(true, Database.isSlotOccupied(slotId2, parkingSlotList))
    }

    @Test
    fun isParkingSlotValidTest() {
        val slotId1 = "B2"
        assertEquals(true, Database.isParkingSlotValid(slotId1, parkingSlotList))

        val slotId2 = "Z7"
        assertEquals(false, Database.isParkingSlotValid(slotId2, parkingSlotList))
    }
}