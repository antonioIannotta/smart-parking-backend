package it.unibo.lss.parking_system.framework.utils

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoCollection
import org.bson.Document

const val defaultMongoAddress =
    "mongodb+srv://testUser:testUser@cluster0.r3hsl.mongodb.net/?retryWrites=true&w=majority"
const val defaultDBName = "test-db"
const val defaultCollectionName = "user-collection"

fun getMongoClient(mongoAddress: String = defaultMongoAddress): MongoClient {

    val mongoClientURI = MongoClientURI(mongoAddress)
    return MongoClient(mongoClientURI)

}

fun getUserCollection(
    mongoClient: MongoClient = getMongoClient(),
    databaseName: String = defaultDBName,
    collectionName: String = defaultCollectionName
): MongoCollection<Document> = mongoClient.getDatabase(databaseName).getCollection(collectionName)