package com.example

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test

class MongoConnectionTest {

    @Test
    fun `test connection to mongo atlas database and user collection existence`() = testApplication {

        application {
            val mongoAddress = environment.config.property("mongo.address").getString()
            val databaseName = environment.config.property("mongo.database.name").getString()
            val collectionName = environment.config.property("mongo.database.collections.user").getString()

            val mongoClientURI = MongoClientURI(mongoAddress)
            val mongoClient = MongoClient(mongoClientURI)

            mongoClient.getDatabase(databaseName).getCollection(collectionName)
        }

    }

}