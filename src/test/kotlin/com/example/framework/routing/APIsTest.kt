package com.example.framework.routing

import com.example.interface_adapter.user.model.ResponseCode
import com.example.interface_adapter.user.model.request.SignInRequestBody
import com.example.interface_adapter.user.model.request.SignUpRequestBody
import com.example.interface_adapter.user.model.response.SigningResponseBody
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
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

    private val testMail = "test@test.it"
    private val testPassword = "Test123!"
    private val testName = "testName"
    private val testSurname = "testSurname"

    @Test
    @Order(1)
    fun `test sign-up API return success code and user jwt`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName, testSurname)

        client.post("/user/sign-up") {
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
    fun `test that sign-up API can't let register 2  user with the same mail`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName, testSurname)

        //sign up user
        client.post("/user/sign-up") {
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
    fun `test that sign-in API return success code and jwt`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val signInRequestBody = SignInRequestBody(testMail, testPassword)

        //sign in user
        client.post("/user/sign-in") {
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
    fun `test that user delete API return success code`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        //get token
        val signInRequestBody = SignInRequestBody(testMail, testPassword)
        val token = client.post("/user/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(signInRequestBody)
        }.body<SigningResponseBody>().token

        //call user delete endpoint
        client.get("/user/delete") {
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