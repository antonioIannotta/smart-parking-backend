package it.unibo.lss.parking_system

import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import it.unibo.lss.parking_system.use_cases.getMongoClient
import it.unibo.lss.parking_system.use_cases.getUserCollection

class MongoConnectionTest {

    @Test
    fun `test connection to mongo atlas database and user collection existence`() = testApplication {

        val mongoClient = getMongoClient()
        getUserCollection(mongoClient)

    }

}