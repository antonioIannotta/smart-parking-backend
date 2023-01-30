package com.example.parkingSlot.database

import com.example.parkingSlot.models.IncrementOccupation
import com.example.parkingSlot.models.SlotOccupation
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import org.bson.conversions.Bson
import java.time.LocalDateTime

object Database {

    private val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
    private val parkingSlotCollection = "Parking slot"
    private val databaseName = "ParkingSystem"

    fun occupySlot(slotOccupation: SlotOccupation) {
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))

        val filter = Filters.eq("id", slotOccupation.slotId)
        val updates = emptyList<Bson>().toMutableList()
        updates.add(Updates.set("occupied", true))
        updates.add(Updates.set("endStop", LocalDateTime.parse(slotOccupation.endStop)))

        val options = UpdateOptions().upsert(true)

        mongoClient.getDatabase(databaseName).getCollection(parkingSlotCollection)
            .updateOne(filter, updates, options)

        mongoClient.close()
    }

    fun incrementOccupation(incrementOccupation: IncrementOccupation) {
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))

        val filter = Filters.eq("id", incrementOccupation.slotId)
        //TODO
    }
}