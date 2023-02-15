package com.example.user.routing

import com.example.user.model.ResponseCode
import com.example.user.model.request.SignUpRequestBody
import com.example.user.model.response.SigningResponseBody
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class APIsTest {

    private val testMail = "test@test.it"
    private val testPassword = "Test123!"
    private val testName = "testName"
    private val testSurname = "testSurname"

    @Test
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

}