package com.example.user.routing

import com.example.user.controller.UserController
import com.example.user.model.request.SignInRequestBody
import com.example.user.model.request.SignUpRequestBody
import com.example.user.model.response.UserInfoResponseBody
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class UserInfoTest {

    //BEGIN: config
    companion object {

        private const val testMail = "test@test.it"
        private const val testPassword = "Test123!"
        private const val testName = "testName"
        private const val testSurname = "testSurname"
        private lateinit var userController: UserController

        @BeforeAll
        @JvmStatic
        fun config() = testApplication {
            //create user controller
            application {
                val tokenSecret = environment.config.property("jwt.secret").getString()
                val mongoAddress = environment.config.property("mongo.address").getString()
                val databaseName = environment.config.property("mongo.database.name").getString()
                val collectionName = environment.config.property("mongo.database.collections.user").getString()
                userController = UserController(mongoAddress, databaseName, collectionName, tokenSecret)
                //register test user
                val signUpRequestBody = SignUpRequestBody(testMail, testPassword, testName, testSurname)
                userController.signUp(signUpRequestBody)
            }
        }

        @AfterAll
        @JvmStatic
        fun deleteTestUser() = testApplication {
            userController.deleteUser(testMail)
        }

    }
    //END config

    @Test
    fun `test that user info return info about the user`() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        //log user and get jwt
        val signInRequestBody = SignInRequestBody(testMail, testPassword)
        val jwt = userController.signIn(signInRequestBody).token
        //get user info
        client.get("/user/info") {
            header(HttpHeaders.Authorization, "Bearer $jwt")
        }.apply {
            val responseBody = call.response.body<UserInfoResponseBody>()
            //user info verification
            assertEquals(responseBody.userInfo?.email ?: "", testMail)
        }
    }

}