package it.unibo.lss.parking_system.framework.utils

import io.ktor.server.testing.*
import org.junit.jupiter.api.Test

class MongoConnectionTest {

    @Test
    fun `test connection to mongo atlas database and user collection existence`() = testApplication {

        val mongoClient = getMongoClient()
        getUserCollection(mongoClient)

    }

}