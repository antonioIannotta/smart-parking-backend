package com.example.user.routing

import com.example.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class UserSignUpTest {

    @Test
    fun testSignUp() = testApplication {

        application {
            configureRouting()
        }

        client.get("/user/sign-up").apply {
            assertEquals(HttpStatusCode.OK, status)
        }

    }

}