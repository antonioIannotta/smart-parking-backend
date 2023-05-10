package it.unibo.lss.smart_parking.framework.routing

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import it.unibo.lss.smart_parking.app.BuildConfig
import it.unibo.lss.smart_parking.framework.module
import it.unibo.lss.smart_parking.framework.utils.getUserCollection
import it.unibo.lss.smart_parking.framework.utils.getUserMongoClient
import it.unibo.lss.smart_parking.user.interface_adapter.UserInterfaceAdapter
import it.unibo.lss.smart_parking.user.interface_adapter.model.ResponseCode
import it.unibo.lss.smart_parking.user.interface_adapter.model.request.SignUpRequestBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserDeleteTest {

    companion object {
        private lateinit var testApp: TestApplication
        private const val testSecret = BuildConfig.HASHING_SECRET
        private var interfaceAdapter =
            UserInterfaceAdapter(getUserCollection(getUserMongoClient(BuildConfig.USER_DB_CONNECTION_STRING)), testSecret)
        private const val userInfoEndpoint = "/user/current"
        private val testMail = "${UUID.randomUUID()}@test.it"
        private const val testPassword = "Test123!"
        private const val testName = "testName"

        @JvmStatic
        @BeforeAll
        fun config() {
            testApp = TestApplication {
                application {
                    module(
                        userDbConnectionString = BuildConfig.USER_DB_CONNECTION_STRING,
                        parkingSlotDbConnectionString = BuildConfig.PARKING_SLOT_DB_CONNECTION_STRING,
                        hashingSecret = BuildConfig.HASHING_SECRET
                    )
                }
            }
        }
    }

    @Test
    fun `test that delete user api delete the selected user`() = testSuspend {
        //register the user and get a valid jwt for him
        val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName)
        val signUpResponse = interfaceAdapter.signUp(signUpRequestBody, testSecret)
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
            //response verification
            assertEquals(HttpStatusCode.OK, call.response.status)
            //check user doesn't exist anymore
            assertEquals(ResponseCode.WRONG_CREDENTIALS.code, interfaceAdapter.getUserInfo(userId).errorCode)
        }
    }

}