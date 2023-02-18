package com.example.user.com.example.use_cases.user

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI

const val defaultMongoAddress =
    "mongodb+srv://testUser:testUser@cluster0.r3hsl.mongodb.net/?retryWrites=true&w=majority"
const val defaultDBName = "test-db"
const val defaultCollectionName = "user-collection"

fun getMongoClient(mongoAddress: String = defaultMongoAddress): MongoClient {

    val mongoClientURI = MongoClientURI(mongoAddress)
    return MongoClient(mongoClientURI)

}

fun getUserCollection(
    mongoClient: MongoClient,
    databaseName: String = defaultDBName,
    collectionName: String = defaultCollectionName
) = mongoClient.getDatabase(databaseName).getCollection(collectionName)