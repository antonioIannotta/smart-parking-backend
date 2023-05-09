package it.unibo.lss.smart_parking.framework

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApplicationTest {
    companion object {
        private const val testSecret = "1234567890"
    }
        @Test
    fun `test application startup and base endpoint connection`() = testApplication {

        application {
            module(testSecret, testSecret)
        }

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
