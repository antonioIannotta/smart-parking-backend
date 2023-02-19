package com.example.framework.routing

import com.example.interface_adapter.user.deleteExistingUser
import com.example.interface_adapter.user.model.request.ChangePasswordRequestBody
import com.example.interface_adapter.user.changePassword
import com.example.interface_adapter.user.userInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.protectedUserRoutes() {

    get("/user/info") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()

        val responseBody = userInfo(userMail)

        if (Objects.isNull(responseBody.userInfo))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    get("/user/{userId?}/parking-slot") {
        call.respondText("You called /user/{userId?}/parking-slot")
    }

    get("/user/delete") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()

        val responseBody = deleteExistingUser(userMail)

        if (responseBody.code != "success")
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    post("/user/change-password") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()
        val requestBody = call.receive<ChangePasswordRequestBody>()

        val responseBody = changePassword(userMail, requestBody.newPassword, requestBody.oldPassword)

        if (responseBody.code != "success")
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

}