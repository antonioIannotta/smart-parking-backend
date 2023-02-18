package com.example

import com.example.user.com.example.use_cases.user.getMongoClient
import com.example.user.com.example.use_cases.user.getUserCollection
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import java.util.*

class MongoConnectionTest {

    @Test
    fun `test connection to mongo atlas database and user collection existence`() = testApplication {

        val mongoClient = getMongoClient()
        getUserCollection(mongoClient)

    }

}