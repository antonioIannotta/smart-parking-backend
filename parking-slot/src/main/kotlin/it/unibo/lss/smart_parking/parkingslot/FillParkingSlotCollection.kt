package it.unibo.lss.smart_parking.parkingslot

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position
import org.bson.Document
import org.bson.types.ObjectId

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
object FillParkingSlotCollection {

    fun eraseAndFillCollection(
        mongoAddress: String,
        databaseName: String,
        collectionName: String,
        itemsCount: Int
    ): List<String> {
        eraseCollection(
            mongoAddress,
            databaseName,
            collectionName
        )
        val mongoClient = MongoClients.create(mongoAddress)
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)
        return fillCollection(
            collection,
            itemsCount
        )
    }

    private fun eraseCollection(
        mongoAddress: String,
        databaseName: String,
        collectionName: String,
    ) {
        val mongoClient = MongoClients.create(mongoAddress)
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)
        collection.drop()
    }

    private fun fillCollection(mongoCollection: MongoCollection<Document>, itemsCount: Int): List<String> =
        (0 until itemsCount).map {
            val newParkingSlotId = ObjectId()
            val newParkingSlot = Document()
                .append("_id", newParkingSlotId)
                .append("occupied", false)
                .append("location", Point(Position(0.0, 0.0)))

            mongoCollection.insertOne(newParkingSlot)
            newParkingSlotId.toString()
        }
}