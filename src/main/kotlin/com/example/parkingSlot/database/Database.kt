package com.example.parkingSlot.database

import com.example.parkingSlot.models.IncrementOccupation
import com.example.parkingSlot.models.ParkingSlot
import com.example.parkingSlot.models.SlotId
import com.example.parkingSlot.models.SlotOccupation
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.conversions.Bson
import java.time.LocalDateTime

/**
 * This object represents the operation that are performed on the database as response of a certain requests by the client
 */
object Database {

    private val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
    private val parkingSlotCollection = "parking-slot"
    private val databaseName = "ParkingSystem"

    /**
     * Function that occupy a certain slot based on the slotOccupation object received as argument
     */
    fun occupySlot(slotOccupation: SlotOccupation, parkingSlotList: MutableList<ParkingSlot>): Boolean {

        var returnValue = false

        returnValue = if (!isSlotOccupied(slotOccupation.slotId, parkingSlotList)) {
            val mongoClient = MongoClient(MongoClientURI(mongoAddress))

            val filter = Filters.eq("id", slotOccupation.slotId)
            val updates = mutableListOf<Bson>()
            updates.add(Updates.set("occupied", true))
            updates.add(Updates.set("endStop", slotOccupation.endStop))
            val options = UpdateOptions().upsert(true)

            mongoClient.getDatabase(databaseName).getCollection(parkingSlotCollection)
                .updateOne(filter, updates, options)

            true
        } else {
            false
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
    fun incrementOccupation(incrementOccupation: IncrementOccupation, parkingSlotList: MutableList<ParkingSlot>): Boolean {

        var returnValue = false

        if (isSlotOccupied(incrementOccupation.slotId, parkingSlotList)) {
            val parkingSlot = getParkingSlot(incrementOccupation.slotId)
            if (isTimeValid(incrementOccupation.endStop, parkingSlot.endStop)) {
                val mongoClient = MongoClient(MongoClientURI(mongoAddress))

                val filter = Filters.eq("id", incrementOccupation.slotId)
                val update = Updates.set("endStop", LocalDateTime.parse(incrementOccupation.endStop))

                mongoClient.getDatabase(databaseName).getCollection(parkingSlotCollection)
                    .updateOne(filter, update)

                mongoClient.close()

                returnValue = true
            } else {
                returnValue = false
            }
        } else {
            returnValue = false
        }

        return returnValue
    }

    /**
     * Function used to check if the time inserted during the increment stop phase is greater than the actual one
     */
    fun isTimeValid(actualTime: String, previousTime: String) =
        LocalDateTime.parse(actualTime).isAfter(LocalDateTime.parse(previousTime))


    /**
     * Function that free a certain slot based on the slotId object received as argument
     */
    fun freeSlot(slotId: SlotId, parkingSlotList: MutableList<ParkingSlot>): Boolean {

        var returnValue = false

        if (isSlotOccupied(slotId.slotId, parkingSlotList)) {
            val mongoClient = MongoClient(MongoClientURI(mongoAddress))

            val filter = Filters.eq("id", slotId.slotId)
            val updates = emptyList<Bson>().toMutableList()
            updates.add(Updates.set("occupied", false))
            updates.add(Updates.set("endStop", ""))

            val options = UpdateOptions().upsert(true)

            mongoClient.getDatabase(databaseName).getCollection(parkingSlotCollection)
                .updateOne(filter, updates, options)

            mongoClient.close()

            returnValue = true

        } else {
            returnValue = false
        }

        return returnValue
    }

    /**
     * Function that returns all the parking slots that are stored into the database
     */
    fun getAllParkingSlots(): MutableList<ParkingSlot> {
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))

        val parkingSlotList = emptyList<ParkingSlot>().toMutableList()

        mongoClient.getDatabase(databaseName).getCollection(parkingSlotCollection).find().forEach {
            document -> parkingSlotList.add(createParkingSlotFromDocument(document))
        }

        return parkingSlotList
    }

    /**
     * Function that returns the parking slot corresponding to the given Id
     */
    fun getParkingSlot(id: String): ParkingSlot {
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))

        val parkingSlot = createParkingSlotFromDocument(mongoClient.getDatabase(databaseName).getCollection(parkingSlotCollection)
            .find().first {
                document -> document["id"].toString() == id
            })

        mongoClient.close()
        return parkingSlot
    }

    /**
     * Function that returns a parking slot given a certain Bson Document
     */
    fun createParkingSlotFromDocument(document: Document): ParkingSlot =
        ParkingSlot(document["id"].toString(),
            document["occupied"].toString().toBoolean(),
            document["endStop"].toString())


}