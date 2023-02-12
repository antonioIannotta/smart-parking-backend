package com.example.user.routing

import com.example.plugins.configureAuthentication
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class AuthenticationTest {

    /**
     * check that protected endpoints give "unauthorized" response to unauthorized users
     */
    @Test
    fun testAPIAuthentication() = testApplication {

        val testUser = "testUser"

        application {
            configureAuthentication()
        }

        client.get("/user/$testUser/info").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }

        client.get("/user/$testUser/parking-slot").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }

        client.get("/user/$testUser/logout").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }

        client.get("/user/$testUser/delete").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }

    }
}