package it.unibo.lss.smart_parking.framework.utils

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document

/*
Copyright (c) 2022-2023 Antonio Iannotta & Luca Bracchi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
const val userDBName = "user"
const val userCollectionName = "user"

const val parkingSlotDBName = "parking-slot"
const val parkingSlotCollectionName = "parking-slot"

fun getUserMongoClient(connectionString: String): MongoClient {
    return MongoClients.create(connectionString)
}

fun getParkingSlotMongoClient(connectionString: String): MongoClient {
    return MongoClients.create(connectionString)
}

fun getUserCollection(
    mongoClient: MongoClient,
    databaseName: String = userDBName,
    collectionName: String = userCollectionName
): MongoCollection<Document> = mongoClient.getDatabase(databaseName).getCollection(collectionName)

fun getParkingSlotCollection(
    mongoClient: MongoClient,
    databaseName: String = parkingSlotDBName,
    collectionName: String = parkingSlotCollectionName
): MongoCollection<Document> = mongoClient.getDatabase(databaseName).getCollection(collectionName)