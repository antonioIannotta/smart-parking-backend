package it.unibo.lss.smart_parking.framework

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import it.unibo.lss.smart_parking.app.BuildConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApplicationTest {
        @Test
    fun `test application startup and base endpoint connection`() = testApplication {

        application {
            module(
                userDbConnectionString = BuildConfig.USER_DB_CONNECTION_STRING,
                parkingSlotDbConnectionString = BuildConfig.PARKING_SLOT_DB_CONNECTION_STRING,
                hashingSecret = BuildConfig.HASHING_SECRET
            )
        }

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
