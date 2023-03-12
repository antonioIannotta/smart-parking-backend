package it.unibo.lss.smart_parking.framework.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.lss.smart_parking.entity.Center
import it.unibo.lss.smart_parking.entity.Position
import it.unibo.lss.smart_parking.entity.StopEnd
import it.unibo.lss.smart_parking.framework.utils.getParkingSlotCollection
import it.unibo.lss.smart_parking.framework.utils.getParkingSlotMongoClient
import it.unibo.lss.smart_parking.framework.utils.getUserCollection
import it.unibo.lss.smart_parking.framework.utils.getUserMongoClient
import it.unibo.lss.smart_parking.interface_adapter.InterfaceAdapter
import it.unibo.lss.smart_parking.interface_adapter.UserInterfaceAdapter
import it.unibo.lss.smart_parking.interface_adapter.model.request.ChangePasswordRequestBody
import java.util.*

/*
Copyright (c) 2022-2023 Antonio Iannotta & Luca Bracchi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
fun Route.protectedUserRoutes() {

    //BEGIN: user endpoints
    get("/user/current") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("userId").asString()

        val mongoClient = getUserMongoClient()
        val interfaceAdapter = UserInterfaceAdapter(getUserCollection(mongoClient))
        val responseBody = interfaceAdapter.getUserInfo(userId)
        mongoClient.close()

        if (Objects.isNull(responseBody.email))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    delete("/user/current") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("userId").asString()

        val mongoClient = getUserMongoClient()
        val interfaceAdapter = UserInterfaceAdapter(getUserCollection(mongoClient))
        val responseBody = interfaceAdapter.deleteUser(userId)
        mongoClient.close()

        if (responseBody.errorCode != null)
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    post("/user/change-password") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("userId").asString()
        val requestBody = call.receive<ChangePasswordRequestBody>()

        val mongoClient = getUserMongoClient()
        val interfaceAdapter = UserInterfaceAdapter(getUserCollection(mongoClient))
        val responseBody = interfaceAdapter.changePassword(userId, requestBody.newPassword, requestBody.currentPassword)
        mongoClient.close()

        if (responseBody.errorCode != null)
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }
    //END: user endpoints

    //BEGIN: parking-slot endpoints
    put("/parking-slot/{id}/occupy") {

        val mongoClient = getParkingSlotMongoClient()
        val interfaceAdapter = InterfaceAdapter(getParkingSlotCollection(mongoClient))

        val slotId = call.parameters["id"]!!
        val stopEnd = call.receive<StopEnd>().stopEnd
        val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()

        val response = interfaceAdapter.occupySlot(userId, slotId, stopEnd)

        mongoClient.close()
        call.respond(response.first, response.second)
    }
    put("/parking-slot/{id}/increment-occupation") {

        val mongoClient = getParkingSlotMongoClient()
        val interfaceAdapter = InterfaceAdapter(getParkingSlotCollection(mongoClient))

        val slotId = call.parameters["id"]!!
        val newEndTime = call.receive<StopEnd>().stopEnd
        val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
        val response = interfaceAdapter.incrementOccupation(userId, slotId, newEndTime)

        mongoClient.close()
        call.respond(response.first, response.second)
    }
    put("/parking-slot/{id}/free") {

        val mongoClient = getParkingSlotMongoClient()
        val interfaceAdapter = InterfaceAdapter(getParkingSlotCollection(mongoClient))

        val slotId = call.parameters["id"]!!
        val response = interfaceAdapter.freeSlot(slotId)

        mongoClient.close()
        call.respond(response.first, response.second)

    }
    get("/parking-slot/") {
        val latitude = call.request.queryParameters["latitude"]?.toDouble()
        val longitude = call.request.queryParameters["longitude"]?.toDouble()
        val radius = call.request.queryParameters["radius"]?.toDouble()

        if (latitude == null || longitude == null || radius == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val mongoClient = getParkingSlotMongoClient()
        val collection = getParkingSlotCollection(mongoClient)

        val interfaceAdapter = InterfaceAdapter(collection)
        val center = Center(Position(latitude, longitude), radius)
        val response = interfaceAdapter.getAllParkingSlotsByRadius(center)

        mongoClient.close()
        call.respond(response)
    }
    get("/parking-slot/current") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("userId").asString()

        val mongoClient = getParkingSlotMongoClient()
        val interfaceAdapter = InterfaceAdapter(getParkingSlotCollection(mongoClient))

        val responseBody = interfaceAdapter.getParkingSlotOccupiedByUser(userId)
        mongoClient.close()

        if (responseBody != null)
            call.respond(HttpStatusCode.OK, responseBody)
        else
            call.respond(
                HttpStatusCode.NotFound,
                interfaceAdapter.createResponse(HttpStatusCode.NotFound, "errorCode", "ParkingSlotNotFound").second
            )
    }
    get("/parking-slot/{id}") {

        val mongoClient = getParkingSlotMongoClient()
        val interfaceAdapter = InterfaceAdapter(getParkingSlotCollection(mongoClient))
        val slotId = call.parameters["id"]!!
        val response = interfaceAdapter.getParkingSlot(slotId)

        mongoClient.close()

        if (response != null)
            call.respond(HttpStatusCode.OK, response)
        else
            call.respond(
                HttpStatusCode.NotFound,
                interfaceAdapter.createResponse(HttpStatusCode.NotFound, "errorCode", "ParkingSlotNotFound").second
            )
    }
    //END: parking-slot endpoints

}