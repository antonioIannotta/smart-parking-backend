package it.unibo.lss.parking_system

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoCollection
import org.bson.Document

object FillParkingSlotCollection {
    private const val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
    const val parkingSlotCollection = "parking-slot"
    private const val parkingSlotTestCollection = "parking-slot-test"
    private const val databaseName = "ParkingSystem"

    private val literals = listOf("A", "B")
    private val numbers = listOf("1", "2", "3", "4", "5")


    fun eraseAndFillCollection(collectionName: String) {
        eraseCollection(collectionName)
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)
        fillCollection(collection)
    }

    private fun eraseCollection(collectionName: String) {
        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)
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
            .append("stopEnd", "")
            .append("latitude", 0.0)
            .append("longitude", 0.0)
            .append("userId", "")
}