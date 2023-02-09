package com.example.user.routing

import com.example.user.controller.UserController
import com.example.user.model.UserInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.exposedUserRoutes() {

    val userController = UserController()

    post("/user/sign-up") {

        //get parameter from request and create new user to register
        val userInfo = call.receive<UserInfo>()
        //register new user to db
        userController.signUp(userInfo)
        //sending response to client
        call.response.status(HttpStatusCode(200, "user registered"))

    }

    get("/user/sign-in") {
        call.respondText("You called /user/sign-in")
    }

}