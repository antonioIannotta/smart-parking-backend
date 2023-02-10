package com.example.user.routing

import com.example.plugins.configureRouting
import com.example.user.model.request.SignInRequestBody
import com.example.user.model.request.SignUpRequestBody
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class APIsTest {

    val testMail = "test@test.it"
    val testPassword = "Test123!"
    val testName = "testName"
    val testSurname = "testSurname"

    @Test
    fun testUserSignUp() = testApplication {

        application {
            configureRouting()
        }

        val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName, testSurname)

        //sign up user
        client.post("/user/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(signUpRequestBody)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

    }

    @Test
    fun testUserSignIn() = testApplication {

        application {
            configureRouting()
        }

        val signInRequestBody = SignInRequestBody(testMail, testPassword)

        //sign up user
        client.post("/user/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(signInRequestBody)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            //TODO: test that request contains the jwt token
        }

    }

}