package it.unibo.lss.smart_parking.framework.utils

import io.github.gzaccaroni.smartparking.app.BuildConfig
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test

class MongoConnectionTest {

    @Test
    fun `test connection to mongo atlas database and user collection existence`() = testApplication {
        getUserMongoClient(BuildConfig.USER_DB_CONNECTION_STRING).use { mongoClient ->
            getUserCollection(mongoClient)
        }
    }

    @Test
    fun `test connection to mongo atlas database and parking slot collection existence`() = testApplication {
        getParkingSlotMongoClient(BuildConfig.PARKING_SLOT_DB_CONNECTION_STRING).use { mongoClient ->
            getParkingSlotCollection(mongoClient)
        }
    }

}