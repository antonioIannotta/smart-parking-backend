package it.unibo.lss.parking_system.framework.routing

import it.unibo.lss.parking_system.interface_adapter.InterfaceAdapter
import it.unibo.lss.parking_system.interface_adapter.changePassword
import it.unibo.lss.parking_system.interface_adapter.deleteExistingUser
import it.unibo.lss.parking_system.interface_adapter.model.request.ChangePasswordRequestBody
import it.unibo.lss.parking_system.interface_adapter.userInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.lss.parking_system.use_cases.IncrementOccupation
import it.unibo.lss.parking_system.use_cases.SlotId
import it.unibo.lss.parking_system.use_cases.SlotOccupation
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