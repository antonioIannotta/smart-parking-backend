package it.unibo.lss.smart_parking.framework.routing

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import it.unibo.lss.smart_parking.framework.module
import it.unibo.lss.smart_parking.framework.utils.getUserCollection
import it.unibo.lss.smart_parking.framework.utils.getUserMongoClient
import it.unibo.lss.smart_parking.user.interface_adapter.UserInterfaceAdapter
import it.unibo.lss.smart_parking.user.interface_adapter.model.request.ChangePasswordRequestBody
import it.unibo.lss.smart_parking.user.interface_adapter.model.request.LoginRequestBody
import it.unibo.lss.smart_parking.user.interface_adapter.model.request.SignUpRequestBody
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ChangePasswordTest {

    companion object {
        private const val testSecret = "1234567890"
        private lateinit var testApp: TestApplication
        private var interfaceAdapter = UserInterfaceAdapter(getUserCollection(getUserMongoClient()), testSecret)
        private const val changePasswordEndpoint = "/user/change-password"
        private const val testMail = "test@test.it"
        private const val testPassword = "Test123!"
        private const val testName = "testName"

        private lateinit var userId: String

        @JvmStatic
        @BeforeAll
        fun config() {
            testApp = TestApplication {
                application {
                    module(testSecret, testSecret)
                }
                //register test user
                val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName)
                val result = interfaceAdapter.signUp(signUpRequestBody, testSecret)
                assertNull(result.errorCode)

                val userId = result.userId
                assertNotNull(userId)
                Companion.userId = userId
            }
        }

        @JvmStatic
        @AfterAll
        fun deleteTestUser() = testApplication {
            interfaceAdapter.deleteUser(userId)
        }

    }

    @Test
    fun `test that change-password api let a user to change his password`() = testSuspend {
        //get a valid jwt for the user and prepare request body
        val loginResult = interfaceAdapter.login(LoginRequestBody(testMail, testPassword), testSecret)
        val userId = loginResult.userId
        val jwt = loginResult.token
        assertNotNull(userId)
        val newPassword = "abcde"
        val changePasswordRequestBody = ChangePasswordRequestBody(testPassword, newPassword)
        //change user's password
        testApp.createClient {
            install(ContentNegotiation) {
                json()
            }
        }.post(changePasswordEndpoint) {
            header(HttpHeaders.Authorization, "Bearer $jwt")
            contentType(ContentType.Application.Json)
            setBody(changePasswordRequestBody)
        }.apply {
            //response verification
            assertEquals(HttpStatusCode.OK, call.response.status)
            //new password verification
            assertEquals(true, interfaceAdapter.validateCredentials(userId, newPassword))
        }
    }

}