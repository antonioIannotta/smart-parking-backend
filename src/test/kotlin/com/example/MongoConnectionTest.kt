package com.example

import com.example.use_cases.user.getMongoClient
import com.example.use_cases.user.getUserCollection
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test

class MongoConnectionTest {

    @Test
    fun `test connection to mongo atlas database and user collection existence`() = testApplication {

        val mongoClient = getMongoClient()
        getUserCollection(mongoClient)

    }

}