package com.example.user.routing

import com.example.user.controller.UserController
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.protectedUserRoutes() {

    val userController = UserController()

    get("/user/{userId?}/info") {
        call.respondText("You called /user/{userId?}/info")
    }

    get("/user/{userId?}/parking-slot") {
        call.respondText("You called /user/{userId?}/parking-slot")
    }

    get("/user/{userId?}/logout") {
        call.respondText("You called /user/{userId?}/logout")
    }

    get("/user/delete") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()

        val operationsResult = userController.deleteUser(userMail)

        call.response.status(HttpStatusCode(operationsResult.code, operationsResult.message))
    }

}