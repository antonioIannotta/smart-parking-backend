package it.unibo.lss.smart_parking.framework.routing

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import it.unibo.lss.smart_parking.entity.UserCredentials
import it.unibo.lss.smart_parking.framework.module
import it.unibo.lss.smart_parking.framework.utils.getUserCollection
import it.unibo.lss.smart_parking.framework.utils.getUserMongoClient
import it.unibo.lss.smart_parking.interface_adapter.UserInterfaceAdapter
import it.unibo.lss.smart_parking.interface_adapter.model.ResponseCode
import it.unibo.lss.smart_parking.interface_adapter.model.request.SignUpRequestBody
import it.unibo.lss.smart_parking.interface_adapter.model.response.SigningResponseBody
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserLoginTest {

    companion object {
        private lateinit var testApp: TestApplication
        private var interfaceAdapter = UserInterfaceAdapter(getUserCollection(getUserMongoClient()))
        private const val userLoginEndpoint = "/user/login"
        private const val testMail = "test@test.it"
        private const val testPassword = "Test123!"
        private const val testName = "testName"
        private const val testSecret = "1234567890"

        private lateinit var userId: String

        @JvmStatic
        @BeforeAll
        fun config() {
            testApp = TestApplication {
                application {
                    module(testSecret)
                }
                //register test user
                val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName)
                val result = interfaceAdapter.signUp(signUpRequestBody, testSecret)
                assertNull(result.errorCode)

                val userId = result.userId
                assertNotNull(userId)
                this@Companion.userId = userId
            }
        }

        @JvmStatic
        @AfterAll
        fun deleteTestUser() = testApplication {
            interfaceAdapter.deleteUser(userId)
        }

    }

    @Test
    fun `test that user login endpoint successfully authenticate user and return a jwt`() = testSuspend {
        //create credentials object
        val credentials = UserCredentials(testMail, testPassword)
        //make request to test response
        testApp.createClient {
            install(ContentNegotiation) {
                json()
            }
        }.post(userLoginEndpoint) {
            contentType(ContentType.Application.Json)
            setBody(credentials)
        }.apply {
            val responseBody = call.response.body<SigningResponseBody>()
            //response verification
            assertEquals(HttpStatusCode.OK, call.response.status)
            assert(responseBody.token is String)
        }
    }

    @Test
    fun `test that user login endpoint return the right error for existing user but wrong password`() = testSuspend {
        //create credentials object
        val credentials = UserCredentials(testMail, "wrong password")
        //make request to test response
        testApp.createClient {
            install(ContentNegotiation) {
                json()
            }
        }.post(userLoginEndpoint) {
            contentType(ContentType.Application.Json)
            setBody(credentials)
        }.apply {
            val responseBody = call.response.body<SigningResponseBody>()
            //response verification
            assertEquals(HttpStatusCode.BadRequest, call.response.status)
            assertEquals(ResponseCode.WRONG_CREDENTIALS.code, responseBody.errorCode)
            assertEquals(null, responseBody.token)
        }
    }

    @Test
    fun `test that user login endpoint return wrong-credential error if user doesn't exist`() = testSuspend {
        //create credentials object
        val credentials = UserCredentials("wrong-user", testPassword)
        //make request to test response
        testApp.createClient {
            install(ContentNegotiation) {
                json()
            }
        }.post(userLoginEndpoint) {
            contentType(ContentType.Application.Json)
            setBody(credentials)
        }.apply {
            val responseBody = call.response.body<SigningResponseBody>()
            //response verification
            assertEquals(HttpStatusCode.BadRequest, call.response.status)
            assertEquals(ResponseCode.WRONG_CREDENTIALS.code, responseBody.errorCode)
            assertEquals(null, responseBody.token)
        }
    }

}