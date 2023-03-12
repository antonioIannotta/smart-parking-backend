package it.unibo.lss.smart_parking.framework.routing

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import it.unibo.lss.smart_parking.framework.module
import it.unibo.lss.smart_parking.framework.utils.getUserCollection
import it.unibo.lss.smart_parking.framework.utils.getUserMongoClient
import it.unibo.lss.smart_parking.interface_adapter.UserInterfaceAdapter
import it.unibo.lss.smart_parking.interface_adapter.model.ResponseCode
import it.unibo.lss.smart_parking.interface_adapter.model.request.SignUpRequestBody
import it.unibo.lss.smart_parking.interface_adapter.model.response.ServerResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserDeleteTest {

    companion object {
        private lateinit var testApp: TestApplication
        private var interfaceAdapter = UserInterfaceAdapter(getUserCollection(getUserMongoClient()))
        private const val userInfoEndpoint = "/user/current"
        private const val testMail = "test@test.it"
        private const val testPassword = "Test123!"
        private const val testName = "testName"
        private const val testSecret = "1234567890"

        @JvmStatic
        @BeforeAll
        fun config() {
            testApp = TestApplication {
                application {
                    module(testSecret)
                }
            }
        }
    }

    @Test
    fun `test that delete user api delete the selected user`() = testSuspend {
        //register the user and get a valid jwt for him
        val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName)
        val signUpResponse = interfaceAdapter.createUser(signUpRequestBody, testSecret)
        assertNull(signUpResponse.errorCode)

        val userId = signUpResponse.userId
        assertNotNull(userId)

        //test that user is signed up
        assertNull(interfaceAdapter.getUserInfo(userId).errorCode)
        //delete user
        testApp.createClient {
            install(ContentNegotiation) {
                json()
            }
        }.delete(userInfoEndpoint) {
            header(HttpHeaders.Authorization, "Bearer ${signUpResponse.token}")
        }.apply {
            val responseBody = call.response.body<ServerResponseBody>()
            //response verification
            assertEquals(HttpStatusCode.OK, call.response.status)
            //check user doesn't exist anymore
            assertEquals(ResponseCode.WRONG_CREDENTIALS.code, interfaceAdapter.getUserInfo(userId).errorCode)
        }
    }

}