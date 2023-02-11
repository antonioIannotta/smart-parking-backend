package com.example.user.routing

import com.example.user.controller.UserController
import com.example.user.model.UserInfo
import com.example.user.model.request.SignInRequestBody
import com.example.user.model.request.SignUpRequestBody
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.exposedUserRoutes(tokenSecret: String) {

    val userController = UserController()

    post("/user/sign-up") {

        //get parameter from request and create new user to register
        val signUpRequestBody = call.receive<SignUpRequestBody>()
        //register new user to db
        val response = userController.signUp(signUpRequestBody)
        //sending response to client
        call.response.status(HttpStatusCode(response.code, response.message))

    }

    post("/user/sign-in") {

        //get parameter from request and create user to login
        val signInRequestBody = call.receive<SignInRequestBody>()
        //get jwt token and user info
        val responseBody = userController.signIn(signInRequestBody, tokenSecret)
        //sending response to client
        if(responseBody.logged) { //TODO: send user info
            call.response.status(HttpStatusCode.OK)
            call.respond(responseBody)
        } else
            call.response.status(HttpStatusCode(400, responseBody.message))

    }

}