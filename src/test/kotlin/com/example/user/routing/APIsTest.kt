package com.example.user.routing

import com.example.plugins.configureAuthentication
import com.example.plugins.configureRouting
import com.example.user.controller.UserController
import com.example.user.model.ResponseCode
import com.example.user.model.request.SignUpRequestBody
import com.example.user.model.response.SigningResponseBody
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import io.ktor.util.*
import io.ktor.util.reflect.*
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.math.sign

class APIsTest {

    private val testMail = "test@test.it"
    private val testPassword = "Test123!"
    private val testName = "testName"
    private val testSurname = "testSurname"

    @Test
    fun testUserAlreadyRegistered() = testApplication {

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

}