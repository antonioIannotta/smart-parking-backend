package com.example.parkingSlot

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoCollection
import org.bson.Document

object FillParkingSlotCollection {
    private const val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
    const val parkingSlotCollection = "parking-slot"
    private const val parkingSlotTestCollection = "parking-slot-test"
    private const val databaseName = "ParkingSystem"

    private val literals = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
        "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    private val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")


    fun eraseAndFillTestCollection() {
        eraseTestCollection()
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(parkingSlotTestCollection)
        fillCollection(collection)
    }

    private fun eraseTestCollection() {
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(parkingSlotTestCollection)
        collection.drop()
    }

    private fun fillCollection(mongoCollection: MongoCollection<Document>) {
        literals.forEach {
                literal -> numbers.forEach {
                number -> mongoCollection.insertOne(createDocument(literal, number))
        }
        }
    }

    private fun createDocument(literal: String, number: String): Document =
        Document()
            .append("id", literal + number)
            .append("occupied", false)
            .append("endStop", "")
}
