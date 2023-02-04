package com.example.user.routing

import com.example.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class UserParkingSlotTest {

    @Test
    fun testParkingSlot() = testApplication {

        application {
            configureRouting()
        }

        client.get("/user/123/parking-slot").apply {
            assertEquals(HttpStatusCode.OK, status)
        }

    }

}