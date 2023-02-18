package com.example.database

import com.example.parkingSlot.database.Database
import com.example.parkingSlot.models.IncrementOccupation
import com.example.parkingSlot.models.ParkingSlot
import com.example.parkingSlot.models.SlotId
import com.example.parkingSlot.models.SlotOccupation
import com.example.parkingSlot.FillParkingSlotCollection
import io.ktor.server.http.content.*
import kotlin.test.Test
import java.time.LocalDateTime
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.bson.Document
import java.time.format.DateTimeFormatter

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

    @Test
    fun occupyTestTrue() {
        FillParkingSlotCollection.eraseAndFillTestCollection()
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = Database.getAllParkingSlots(parkingSlotTestCollection)
        val slotOccupation = SlotOccupation("C2", LocalDateTime.now().plusHours(3).toString())

        val returnValue = Database.occupySlot(parkingSlotTestCollection, slotOccupation, parkingSlotList)
        assertEquals(true, returnValue)
    }

    @Test
    fun occupyTestFalse() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = Database.getAllParkingSlots(parkingSlotTestCollection)
        val slotOccupation = SlotOccupation("C2", LocalDateTime.now().plusHours(3).toString())

        val returnValue = Database.occupySlot(parkingSlotTestCollection, slotOccupation, parkingSlotList)
        assertEquals(false, returnValue)
    }

    @Test
    fun incrementOccupationTestTrue() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = Database.getAllParkingSlots(parkingSlotTestCollection)
        val parkingSlot = Database.getParkingSlot(parkingSlotTestCollection, "C2")
        val newEndStop = LocalDateTime.parse(parkingSlot.endStop).plusHours(2).toString()
        val incrementOccupation = IncrementOccupation(parkingSlot.id, newEndStop)

        val returnValue = Database.incrementOccupation(parkingSlotTestCollection, incrementOccupation, parkingSlotList)

        assertEquals(true, returnValue)
    }

    @Test
    fun incrementOccupationTestFalseTimeNotValid() { //HERE
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = Database.getAllParkingSlots(parkingSlotTestCollection)
        val parkingSlot = Database.getParkingSlot(parkingSlotTestCollection, "C2")

        println(parkingSlot.endStop)
        val newEndStop = LocalDateTime.parse(parkingSlot.endStop).minusHours(2).toString()
        val incrementOccupation = IncrementOccupation(parkingSlot.id, newEndStop)

        val returnValue = Database.incrementOccupation(parkingSlotTestCollection, incrementOccupation, parkingSlotList)

        assertEquals(false, returnValue)
    }

    @Test
    fun incrementOccupationTestFalseParkingSlotNotValid() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = Database.getAllParkingSlots(parkingSlotTestCollection)
        val incrementOccupation = IncrementOccupation("A989", LocalDateTime.MAX.toString())

        val retrunValue = Database.incrementOccupation(parkingSlotTestCollection, incrementOccupation, parkingSlotList)
        assertEquals(false, retrunValue)
    }

    @Test
    fun freeSlotTestTrue() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = Database.getAllParkingSlots(parkingSlotTestCollection)
        val slotId = SlotId("C2")

        val returnValue = Database.freeSlot(parkingSlotTestCollection, slotId, parkingSlotList)

        assertEquals(true, returnValue)
    }

    @Test
    fun freeSlotTestFalseParkingSlotFree() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = Database.getAllParkingSlots(parkingSlotTestCollection)
        val slotId = SlotId("C2")

        val returnValue = Database.freeSlot(parkingSlotTestCollection, slotId, parkingSlotList)
        assertEquals(false, returnValue)
    }

    @Test
    fun freeSlotTestFalseParkingSlotNotValid() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = Database.getAllParkingSlots(parkingSlotTestCollection)
        val slotId = SlotId("A90")

        val returnValue = Database.freeSlot(parkingSlotTestCollection, slotId, parkingSlotList)
        assertEquals(false, returnValue)
    }

}