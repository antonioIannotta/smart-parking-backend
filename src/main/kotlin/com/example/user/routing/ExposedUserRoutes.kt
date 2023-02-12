package com.example.user.routing

import com.example.user.controller.UserController
import com.example.user.model.request.RecoverMailRequestBody
import com.example.user.model.request.SignInRequestBody
import com.example.user.model.request.SignUpRequestBody
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.exposedUserRoutes(tokenSecret: String) {

    val userController = UserController()

    post("/user/sign-up") {

        //get parameter from request and create new user to register
        val signUpRequestBody = call.receive<SignUpRequestBody>()
        //register new user to db
        val responseBody = userController.signUp(signUpRequestBody, tokenSecret)
        //sending response to client
        if (Objects.isNull(responseBody.token))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    post("/user/sign-in") {

        //get parameter from request and create user to login
        val signInRequestBody = call.receive<SignInRequestBody>()
        //get jwt token and user info
        val responseBody = userController.signIn(signInRequestBody, tokenSecret)
        //sending response to client
        if (Objects.isNull(responseBody.token))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    post("/user/recover-password") {

        val userMail = call.receive<RecoverMailRequestBody>().email
        val responseBody = userController.recoverPassword(userMail, tokenSecret)
        //sending response to client
        if (responseBody.code != "success")
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

}