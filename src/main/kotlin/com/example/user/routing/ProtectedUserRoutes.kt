package com.example.user.routing

import com.example.user.controller.UserController
import com.example.user.model.request.ChangePasswordRequestBody
import com.example.user.model.request.RecoverMailRequestBody
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.protectedUserRoutes() {

    val userController = UserController()

    get("/user/info") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()

        val responseBody = userController.userInfo(userMail)

        if(responseBody.foundInfo) {
            call.response.status(HttpStatusCode.OK)
            call.respond(responseBody)
        } else
            call.response.status(HttpStatusCode(400, responseBody.message))
    }

    get("/user/{userId?}/parking-slot") {
        call.respondText("You called /user/{userId?}/parking-slot")
    }

    get("/user/delete") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()

        val operationsResult = userController.deleteUser(userMail)

        call.response.status(HttpStatusCode(operationsResult.code, operationsResult.message))
    }

    post("/user/change-password") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()
        val requestBody = call.receive<ChangePasswordRequestBody>()

        val httpStatusCode = userController.changePassword(userMail, requestBody.newPassword, requestBody.oldPassword)

        call.response.status(httpStatusCode)
    }

}