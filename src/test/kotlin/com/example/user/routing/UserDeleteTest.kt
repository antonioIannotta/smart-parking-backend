package com.example.user.routing

import com.example.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class UserDeleteTest {

    @Test
    fun testDeleteUser() = testApplication {

        application {
            configureRouting()
        }

        client.get("/user/123/delete").apply {
            assertEquals(HttpStatusCode.OK, status)
        }

    }

}