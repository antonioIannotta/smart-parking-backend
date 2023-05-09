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
import it.unibo.lss.smart_parking.interface_adapter.model.request.LoginRequestBody
import it.unibo.lss.smart_parking.interface_adapter.model.request.SignUpRequestBody
import it.unibo.lss.smart_parking.interface_adapter.model.response.UserInfoResponseBody
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserInfoTest {

    companion object {
        private lateinit var testApp: TestApplication
        private const val testSecret = "1234567890"
        private var interfaceAdapter = UserInterfaceAdapter(getUserCollection(getUserMongoClient()), testSecret)
        private const val userInfoEndpoint = "/user/current"
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
    fun `test that user info return info about the user`() = testSuspend {
        //log user and get jwt
        val jwt = interfaceAdapter.login(LoginRequestBody(testMail, testPassword), testSecret).token
        //get user info
        testApp.createClient {
            install(ContentNegotiation) {
                json()
            }
        }.get(userInfoEndpoint) {
            header(HttpHeaders.Authorization, "Bearer $jwt")
        }.apply {
            val responseBody = call.response.body<UserInfoResponseBody>()
            //user info verification
            assertEquals(testMail, responseBody.email)
        }
    }

}