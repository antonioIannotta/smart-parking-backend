package com.example.parkingSlot

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoCollection
import org.bson.Document
import java.time.LocalDateTime

object FillParkingSlotCollection {
    val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
    val parkingSlotCollection = "parking-slot"
    val parkingSlotTestCollection = "parking-slot-test"
    val databaseName = "ParkingSystem"

    val literals = listOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
        "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    val numbers = listOf<String>("1", "2", "3", "4", "5", "6", "7", "8", "9")


    fun eraseAndFillTestCollection() {
        eraseTestCollection()
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(parkingSlotTestCollection)
        fillCollection(literals, numbers, collection)
    }

    fun eraseTestCollection() {
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(parkingSlotTestCollection)
        collection.drop()
    }

    private fun fillCollection(literals: List<String>, numbers: List<String>, mongoCollection: MongoCollection<Document>) {
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
