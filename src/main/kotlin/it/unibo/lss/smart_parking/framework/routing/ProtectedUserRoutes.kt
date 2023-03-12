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

        val responseBody = getUserMongoClient().use { mongoClient ->
            val interfaceAdapter = UserInterfaceAdapter(getUserCollection(mongoClient))
            interfaceAdapter.getUserInfo(userId)
        }

        if (Objects.isNull(responseBody.email))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    delete("/user/current") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("userId").asString()

        val responseBody = getUserMongoClient().use { mongoClient ->
            val interfaceAdapter = UserInterfaceAdapter(getUserCollection(mongoClient))
            interfaceAdapter.deleteUser(userId)
        }

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

        val responseBody = getUserMongoClient().use { mongoClient ->
            val interfaceAdapter = UserInterfaceAdapter(getUserCollection(mongoClient))
            interfaceAdapter.changePassword(userId, requestBody.newPassword, requestBody.currentPassword)
        }

        if (responseBody.errorCode != null)
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }
    //END: user endpoints

    //BEGIN: parking-slot endpoints
    put("/parking-slot/{id}/occupy") {

        val slotId = call.parameters["id"]!!
        val stopEnd = call.receive<StopEnd>().stopEnd
        val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()

        val response = getUserMongoClient().use { mongoClient ->
            val interfaceAdapter = InterfaceAdapter(getParkingSlotCollection(mongoClient))
            interfaceAdapter.occupyParkingSlot(userId, slotId, stopEnd)
        }

        call.respond(response.first, response.second)
    }
    put("/parking-slot/{id}/increment-occupation") {


        val slotId = call.parameters["id"]!!
        val newEndTime = call.receive<StopEnd>().stopEnd
        val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
        val response = getParkingSlotMongoClient().use { mongoClient ->
            val interfaceAdapter = InterfaceAdapter(getParkingSlotCollection(mongoClient))
            interfaceAdapter.incrementParkingSlotOccupation(userId, slotId, newEndTime)
        }

        call.respond(response.first, response.second)
    }
    put("/parking-slot/{id}/free") {

        val slotId = call.parameters["id"]!!
        val response = getParkingSlotMongoClient().use { mongoClient ->
            val interfaceAdapter = InterfaceAdapter(getParkingSlotCollection(mongoClient))
            interfaceAdapter.freeParkingSlot(slotId)
        }

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

        val center = Center(Position(latitude, longitude), radius)
        val response = getParkingSlotMongoClient().use { mongoClient ->
            val collection = getParkingSlotCollection(mongoClient)
            val interfaceAdapter = InterfaceAdapter(collection)
            interfaceAdapter.getAllParkingSlotsByRadius(center)
        }

        call.respond(response)
    }
    get("/parking-slot/current") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("userId").asString()

        val responseBody = getParkingSlotMongoClient().use { mongoClient ->
            val interfaceAdapter = InterfaceAdapter(getParkingSlotCollection(mongoClient))
            interfaceAdapter.getParkingSlotOccupiedByUser(userId)
        }

        if (responseBody != null)
            call.respond(HttpStatusCode.OK, responseBody)
        else
            call.respond(
                HttpStatusCode.NotFound,
                mapOf(
                    "errorCode" to "ParkingSlotNotFound"
                )
            )
    }
    get("/parking-slot/{id}") {

        val slotId = call.parameters["id"]!!
        val response = getParkingSlotMongoClient().use { mongoClient ->
            val interfaceAdapter = InterfaceAdapter(getParkingSlotCollection(mongoClient))
            interfaceAdapter.getParkingSlot(slotId)
        }

        if (response != null)
            call.respond(HttpStatusCode.OK, response)
        else
            call.respond(
                HttpStatusCode.NotFound,
                mapOf(
                    "errorCode" to "ParkingSlotNotFound"
                )
            )
    }
    //END: parking-slot endpoints

}