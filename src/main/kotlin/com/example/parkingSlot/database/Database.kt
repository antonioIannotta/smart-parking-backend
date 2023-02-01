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

        var returnFalue = false


        /*val mongoClient = MongoClient(MongoClientURI(mongoAddress))

        val filter = Filters.eq("id", slotOccupation.slotId)
        val updates = emptyList<Bson>().toMutableList()
        updates.add(Updates.set("occupied", true))
        updates.add(Updates.set("endStop", LocalDateTime.parse(slotOccupation.endStop)))

        val options = UpdateOptions().upsert(true)

        mongoClient.getDatabase(databaseName).getCollection(parkingSlotCollection)
            .updateOne(filter, updates, options)

        mongoClient.close()*/

        return false
    }

    /**
     * Function that increment a certain slot based on the incrementOccupation object received as argument
     */
    fun incrementOccupation(incrementOccupation: IncrementOccupation) {
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))

        val filter = Filters.eq("id", incrementOccupation.slotId)
        val update = Updates.set("endStop", LocalDateTime.parse(incrementOccupation.endStop))

        mongoClient.getDatabase(databaseName).getCollection(parkingSlotCollection)
            .updateOne(filter, update)

        mongoClient.close()
    }

    /**
     * Function that free a certain slot based on the slotId object received as argument
     */
    fun freeSlot(slotId: SlotId) {
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))

        val filter = Filters.eq("id", slotId.slotId)
        val updates = emptyList<Bson>().toMutableList()
        updates.add(Updates.set("occupied", false))
        updates.add(Updates.set("endStop", ""))

        val options = UpdateOptions().upsert(true)

        mongoClient.getDatabase(databaseName).getCollection(parkingSlotCollection)
            .updateOne(filter, updates, options)

        mongoClient.close()
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

    fun replaceSlot(parkingSlot: ParkingSlot, parkingSlotList: MutableList<ParkingSlot>): MutableList<ParkingSlot> {

        if (!isParkingSlotValid(parkingSlot, parkingSlotList)) {
            return parkingSlotList
        }

        parkingSlotList.remove(parkingSlotList.first {
            ps -> ps.id == parkingSlot.id
        })
        parkingSlotList.add(parkingSlot)

        return parkingSlotList
    }

    fun isParkingSlotValid(parkingSlot: ParkingSlot, parkingSlotList: MutableList<ParkingSlot>) =
        parkingSlotList.count {
            ps -> ps.id == parkingSlot.id
        } == 1

}