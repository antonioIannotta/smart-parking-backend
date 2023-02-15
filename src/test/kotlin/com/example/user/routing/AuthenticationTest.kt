package com.example.user.routing

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

/**
 * checks that protected endpoints give "unauthorized" response to unauthorized users
 */
class AuthenticationTest {

    //BEGIN: exposed APIs
    @Test
    fun `test that sign-up API doesn't require authentication`() = testApplication {
        client.post("/user/sign-up").apply {
            assertNotEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `test that sign-ip API doesn't require authentication`() = testApplication {
        client.post("/user/sign-in").apply {
            assertNotEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `test that recover password API doesn't require authentication`() = testApplication {
        client.post("/user/recover-password").apply {
            assertNotEquals(HttpStatusCode.Unauthorized, status)
        }
    }
    //END: exposed APIs

    //BEGIN: protected APIs
    @Test
    fun `test that user info API requires authentication`() = testApplication {
        client.get("/user/info").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    //TODO: parking slot test

    @Test
    fun `test that delete user API requires authentication`() = testApplication {
        client.get("/user/delete").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `test that change password API requires authentication`() = testApplication {
        client.post("/user/change-password").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }
    //BEGIN: protected APIs

}