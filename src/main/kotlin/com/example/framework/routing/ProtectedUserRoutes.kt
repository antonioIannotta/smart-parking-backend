package com.example.framework.routing

import interface_adapter.InterfaceAdapter
import interface_adapter.changePassword
import interface_adapter.deleteExistingUser
import interface_adapter.model.request.ChangePasswordRequestBody
import interface_adapter.userInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import use_cases.IncrementOccupation
import use_cases.SlotId
import use_cases.SlotOccupation
import java.util.*

fun Route.protectedUserRoutes() {

    //BEGIN: user endpoints
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
    //END: user endpoints

    //BEGIN: parking-slot endpoints
    get("/user/sign-up") {  }
    get("/user/sign-in") {  }
    get("/user/{userId?}/parking-slot") {  }
    get("/user/{userId?}/logout") {  }
    get("/user/{userId?}/delete") {  }
    put("/parking-slot/occupy") {

        val slotOccupation = call.receive<SlotOccupation>()
        val response = InterfaceAdapter.occupy(slotOccupation)
        call.respond(response.first, response.second)
    }
    put("/parking-slot/increment-occupation") {

        val incrementOccupation = call.receive<IncrementOccupation>()
        val response = InterfaceAdapter.incrementOccupation(incrementOccupation)
        call.respond(response.first, response.second)
    }
    put("/parking-slot/free") {

        val slotId = call.receive<SlotId>()
        val response = InterfaceAdapter.free(slotId)
        call.respond(response.first, response.second)

    }
    get("/parking-slot/") {

        val response = InterfaceAdapter.getParkingSlotList()
        call.respond(response)
    }
    get("/parking-slot/") {

        val slotId = call.receive<SlotId>()
        val response = InterfaceAdapter.getParkingSlot(slotId)
        call.respond(response)
    }
    //END: parking-slot endpoints

}