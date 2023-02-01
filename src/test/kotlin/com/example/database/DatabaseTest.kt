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

    var parkingSlotList =
        mutableListOf<ParkingSlot>(ParkingSlot("A1", false, ""),
            ParkingSlot("B2", false, ""),
            ParkingSlot("C3", true, LocalDateTime.now().toString()))

    @Test
    fun isValidParkingSlotTest() {
        val newParkingSlot = ParkingSlot("A1", true, LocalDateTime.MIN.toString())
        assertEquals(true, Database.isParkingSlotValid(newParkingSlot, parkingSlotList))

        val newParkingSlot2 = ParkingSlot("Z5", true, LocalDateTime.MIN.toString())
        assertEquals(false, Database.isParkingSlotValid(newParkingSlot2, parkingSlotList))
    }


    @Test
    fun replaceSlotTest() {
        val newParkingSlot = ParkingSlot("A1", true, LocalDateTime.MIN.toString())
        parkingSlotList = Database.replaceSlot(newParkingSlot, parkingSlotList)
        assertContains(Database.replaceSlot(newParkingSlot, parkingSlotList), newParkingSlot)

        val newParkingSlot2 = ParkingSlot("Z5", true, LocalDateTime.MIN.toString())
        assertNotEquals(true, Database.replaceSlot(newParkingSlot2, parkingSlotList).contains(newParkingSlot2))

    }

    @Test
    fun createParkingSlotFromDocumentTest() {
        val parkingSlotDocument = Document().append("id", "A9").append("occupied", "false").append("endStop", "")
        val parkingSlot = ParkingSlot("A9", false, "")
        val parkingSlotFromDocument = Database.createParkingSlotFromDocument(parkingSlotDocument)

        assertEquals(parkingSlot.id, parkingSlotFromDocument.id)
        assertEquals(parkingSlot.occupied, parkingSlotFromDocument.occupied)
        assertEquals(parkingSlot.endStop, parkingSlotFromDocument.endStop)
    }




}