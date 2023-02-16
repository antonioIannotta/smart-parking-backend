package com.example.user.routing

import com.example.user.controller.UserController
import com.example.user.model.request.SignInRequestBody
import com.example.user.model.request.SignUpRequestBody
import io.ktor.server.testing.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class UserInfoTest {

    //BEGIN: config
    companion object {

        private val testMail = "test@test.it"
        private val testPassword = "Test123!"
        private val testName = "testName"
        private val testSurname = "testSurname"
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
        //register user
        val signInRequestBody = SignInRequestBody(testMail, testPassword)
        userController.signIn(signInRequestBody)
        //get user info
        val userInfoResponseBody = userController.userInfo(testMail)
        //user info verification
        assertEquals(userInfoResponseBody.userInfo?.email ?: "", testMail)
    }

}