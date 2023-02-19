package com.example.framework.routing


import com.example.framework.module
import entity.UserCredentials
import interface_adapter.model.ResponseCode
import interface_adapter.model.request.SignInRequestBody
import interface_adapter.model.request.SignUpRequestBody
import interface_adapter.model.response.SigningResponseBody
import interface_adapter.signIn
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

/**
 * test all user APIs
 * tests are ordered to test all the creation-usage-deletion flow of a user
 */
@TestMethodOrder(OrderAnnotation::class)
class APIsTest {

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
            }
        }
    }

    @Test
    @Order(1)
    fun `test sign-up API return success code and user jwt`() = testSuspend {

        val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName, testSurname)

        testApp.createClient {
            install(ContentNegotiation.toString()) {
                json()
            }
        }.post("/user/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(signUpRequestBody)
        }.apply {
            val responseBody = call.response.body<SigningResponseBody>()
            assertEquals(HttpStatusCode.OK, call.response.status)
            assertEquals(ResponseCode.SUCCESS.code, responseBody.code)
            assert(responseBody.token is String)
        }

    }

    @Test
    @Order(2)
    fun `test that sign-up API can't let register 2  user with the same mail`() = testSuspend {

        val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName, testSurname)

        //sign up user
        testApp.createClient {
            install(ContentNegotiation) {
                json()
            }
        }.post("/user/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(signUpRequestBody)
        }.apply {
            val responseBody = call.response.body<SigningResponseBody>()
            assertEquals(HttpStatusCode.BadRequest, call.response.status)
            assertEquals(ResponseCode.ALREADY_REGISTERED.code, responseBody.code)
            assertEquals(null, responseBody.token)
        }
    }

    @Test
    @Order(3)
    fun `test that sign-in API return success code and jwt`() = testSuspend {

        val signInRequestBody = SignInRequestBody(testMail, testPassword)

        //sign in user
        testApp.createClient {
            install(ContentNegotiation) {
                json()
            }
        }.post("/user/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(signInRequestBody)
        }.apply {
            val responseBody = call.response.body<SigningResponseBody>()
            assertEquals(HttpStatusCode.OK, call.response.status)
            assertEquals(ResponseCode.SUCCESS.code, responseBody.code)
            assert(responseBody.token is String)
        }

    }

    @Test
    @Order(4)
    fun `test that user delete API return success code`() = testSuspend {

        //get token
        val token = signIn(UserCredentials(testMail, testPassword), testSecret).token

        //call user delete endpoint
        testApp.createClient {
            install(ContentNegotiation) {
                json()
            }
        }.get("/user/delete") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        print("stop")
//            .apply {
//            val responseBody = call.response.body<ServerResponseBody>()
//            assertEquals(HttpStatusCode.OK, call.response.status)
//            assertEquals(ResponseCode.SUCCESS.code, responseBody.code)
//        }

    }

    @Test
    @Order(5)
    fun `test that user delete API return error code if user doesn't exist`() = testApplication {
        //TODO
    }

}