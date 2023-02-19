package com.example.parkingSlot

import com.example.parkingSlot.use_cases.ParkingSlotUseCases
import com.example.parkingSlot.entity.ParkingSlot
import kotlin.test.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.bson.Document

class ParkingSlotUseCasesTest {

    private var parkingSlotList =
        mutableListOf<ParkingSlot>(
            ParkingSlot("A1", false, ""),
            ParkingSlot("B2", false, ""),
            ParkingSlot("C3", true, LocalDateTime.now().toString())
        )


    @Test
    fun createParkingSlotFromDocumentTest() {
        val parkingSlotDocument = Document().append("id", "A9").append("occupied", "false").append("endStop", "")
        val parkingSlot = ParkingSlot("A9", false, "")
        val parkingSlotFromDocument = ParkingSlotUseCases.createParkingSlotFromDocument(parkingSlotDocument)

        assertEquals(parkingSlot.id, parkingSlotFromDocument.id)
        assertEquals(parkingSlot.occupied, parkingSlotFromDocument.occupied)
        assertEquals(parkingSlot.endStop, parkingSlotFromDocument.endStop)
    }

    @Test
    fun isOccupiedSlotTest() {
        val slotId1 = "B2"
        assertEquals(false, ParkingSlotUseCases.isSlotOccupied(slotId1, parkingSlotList))

        val slotId2 = "C3"
        assertEquals(true, ParkingSlotUseCases.isSlotOccupied(slotId2, parkingSlotList))
    }

    @Test
    fun isParkingSlotValidTest() {
        val slotId1 = "B2"
        assertEquals(true, ParkingSlotUseCases.isParkingSlotValid(slotId1, parkingSlotList))

        val slotId2 = "Z7"
        assertEquals(false, ParkingSlotUseCases.isParkingSlotValid(slotId2, parkingSlotList))
    }

    /*
    @Test
    fun occupyTestTrue() {
        FillParkingSlotCollection.eraseAndFillTestCollection()
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(parkingSlotTestCollection)
        val slotOccupation = SlotOccupation("C2", LocalDateTime.now().plusHours(3).toString())

        val returnValue = ParkingSlotUseCases.occupySlot(parkingSlotTestCollection, slotOccupation, parkingSlotList)
        assertEquals(true, returnValue)
    }

    @Test
    fun occupyTestFalse() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(parkingSlotTestCollection)
        val slotOccupation = SlotOccupation("C2", LocalDateTime.now().plusHours(3).toString())

        val returnValue = ParkingSlotUseCases.occupySlot(parkingSlotTestCollection, slotOccupation, parkingSlotList)
        assertEquals(false, returnValue)
    }

    @Test
    fun incrementOccupationTestTrue() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(parkingSlotTestCollection)
        val parkingSlot = ParkingSlotUseCases.getParkingSlot(parkingSlotTestCollection, "C2")
        val newEndStop = LocalDateTime.parse(parkingSlot.endStop).plusHours(2).toString()
        val incrementOccupation = IncrementOccupation(parkingSlot.id, newEndStop)

        val returnValue = ParkingSlotUseCases.incrementOccupation(parkingSlotTestCollection, incrementOccupation, parkingSlotList)

        assertEquals(true, returnValue)
    }

    @Test
    fun incrementOccupationTestFalseTimeNotValid() { //HERE
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(parkingSlotTestCollection)
        val parkingSlot = ParkingSlotUseCases.getParkingSlot(parkingSlotTestCollection, "C2")

        println(parkingSlot.endStop)
        val newEndStop = LocalDateTime.parse(parkingSlot.endStop).minusHours(2).toString()
        val incrementOccupation = IncrementOccupation(parkingSlot.id, newEndStop)

        val returnValue = ParkingSlotUseCases.incrementOccupation(parkingSlotTestCollection, incrementOccupation, parkingSlotList)

        assertEquals(false, returnValue)
    }

    @Test
    fun incrementOccupationTestFalseParkingSlotNotValid() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(parkingSlotTestCollection)
        val incrementOccupation = IncrementOccupation("A989", LocalDateTime.MAX.toString())

        val retrunValue = ParkingSlotUseCases.incrementOccupation(parkingSlotTestCollection, incrementOccupation, parkingSlotList)
        assertEquals(false, retrunValue)
    }

    @Test
    fun freeSlotTestTrue() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(parkingSlotTestCollection)
        val slotId = SlotId("C2")

        val returnValue = ParkingSlotUseCases.freeSlot(parkingSlotTestCollection, slotId, parkingSlotList)

        assertEquals(true, returnValue)
    }

    @Test
    fun freeSlotTestFalseParkingSlotFree() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(parkingSlotTestCollection)
        val slotId = SlotId("C2")

        val returnValue = ParkingSlotUseCases.freeSlot(parkingSlotTestCollection, slotId, parkingSlotList)
        assertEquals(false, returnValue)
    }

    @Test
    fun freeSlotTestFalseParkingSlotNotValid() {
        Thread.sleep(20000)
        val parkingSlotTestCollection = "parking-slot-test"
        val parkingSlotList = ParkingSlotUseCases.getAllParkingSlots(parkingSlotTestCollection)
        val slotId = SlotId("A90")

        val returnValue = ParkingSlotUseCases.freeSlot(parkingSlotTestCollection, slotId, parkingSlotList)
        assertEquals(false, returnValue)
    }
    */
}