package com.example.framework.routing

import com.example.entity.user.UserCredentials
import com.example.framework.module
import com.example.interface_adapter.user.deleteExistingUser
import com.example.interface_adapter.user.model.request.SignUpRequestBody
import com.example.interface_adapter.user.model.response.UserInfoResponseBody
import com.example.interface_adapter.user.signUp
import com.example.interface_adapter.user.signIn
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class UserInfoTest {

    companion object {
        private lateinit var testApp: TestApplication
        private const val testMail = "test@test.it"
        private const val testPassword = "Test123!"
        private const val testName = "testName"
        private const val testSurname = "testSurname"
        private const val testSecret = "1234567890"

        @JvmStatic
        @BeforeAll
        fun config() {
            testApp = TestApplication {
                application {
                    module(testSecret)
                }
                //register test user
                val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName, testSurname)
                signUp(signUpRequestBody, testSecret)
            }
        }

        @JvmStatic
        @AfterAll
        fun deleteTestUser() = testApplication {
            deleteExistingUser(testMail)
        }

    }

    @Test
    fun `test that user info return info about the user`() = testSuspend {
        //log user and get jwt
        val credentials = UserCredentials(testMail, testPassword)
        val jwt = signIn(credentials, testSecret).token
        //get user info
        testApp.createClient {
            install(ContentNegotiation) {
                json()
            }
        }.get("/user/info") {
            header(HttpHeaders.Authorization, "Bearer $jwt")
        }.apply {
            val responseBody = call.response.body<UserInfoResponseBody>()
            //user info verification
            assertEquals(testMail, responseBody.userInfo?.email)
        }
    }

}