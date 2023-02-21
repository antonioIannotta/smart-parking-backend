package it.unibo.lss.parking_system.framework.routing

import it.unibo.lss.parking_system.framework.module
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

/**
 * checks that protected endpoints give "unauthorized" response to unauthorized users
 */
class AuthenticationTest {

    companion object {
        private lateinit var testApp: TestApplication

        @JvmStatic
        @BeforeAll
        fun config() {
            testApp = TestApplication {
                application {
                    module()
                }
            }
        }
    }

    //BEGIN: exposed APIs
    @Test
    fun `test that sign-up API doesn't require authentication`() = testSuspend {
        testApp.client.post("/user/sign-up").apply {
            assertNotEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `test that sign-ip API doesn't require authentication`() = testSuspend {
        testApp.client.post("/user/login").apply {
            assertNotEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `test that recover password API doesn't require authentication`() = testSuspend {
        testApp.client.post("/user/recover-password").apply {
            assertNotEquals(HttpStatusCode.Unauthorized, status)
        }
    }
    //END: exposed APIs

    //BEGIN: protected APIs
    @Test
    fun `test that user info API requires authentication`() = testSuspend {
        testApp.client.get("/user/info").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    //TODO: parking slot test

    @Test
    fun `test that delete user API requires authentication`() = testSuspend {
        testApp.client.get("/user/delete").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `test that change password API requires authentication`() = testSuspend {
        testApp.client.post("/user/change-password").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }
    //END: protected APIs

}