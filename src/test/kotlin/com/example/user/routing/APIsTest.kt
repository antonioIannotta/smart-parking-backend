package com.example.user.routing

import com.example.plugins.configureRouting
import com.example.user.model.SignUpRequestBody
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class UserSignUpTest {

    val testMail = "test@test.it"
    val testPassword = "Test123!"

    @Test
    fun testSignUp() = testApplication {

        application {
            configureRouting()
        }

        //sign up user
        client.post("/user/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(SignUpRequestBody(,))
        }.apply {
            print("stop")
        }

        //delete signed up user

    }

}