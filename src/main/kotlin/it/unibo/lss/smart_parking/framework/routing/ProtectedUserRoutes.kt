package it.unibo.lss.smart_parking.framework.routing

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
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
import it.unibo.lss.smart_parking.framework.utils.getUserCollection
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
const val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
const val databaseName = "ParkingSystem"
const val collectionName = "parking-slot"
fun Route.protectedUserRoutes() {

    //BEGIN: user endpoints
    get("/user/current") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()

        val interfaceAdapter = UserInterfaceAdapter(getUserCollection())
        val responseBody = interfaceAdapter.getUserInfo(userMail)

        if (Objects.isNull(responseBody.email))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    delete("/user/current") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()

        val interfaceAdapter = UserInterfaceAdapter(getUserCollection())
        val responseBody = interfaceAdapter.deleteUser(userMail)

        if (responseBody.errorCode != null)
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    post("/user/change-password") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()
        val requestBody = call.receive<ChangePasswordRequestBody>()

        val interfaceAdapter = UserInterfaceAdapter(getUserCollection())
        val responseBody = interfaceAdapter.changePassword(userMail, requestBody.newPassword, requestBody.currentPassword)

        if (responseBody.errorCode != null)
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }
    //END: user endpoints

    //BEGIN: parking-slot endpoints
    put("/parking-slot/{id}/occupy") {

        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)

        val interfaceAdapter = InterfaceAdapter(collection)

        val slotId = call.parameters["id"]!!
        val stopEnd = call.receive<StopEnd>().stopEnd
        val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("email").asString()
        val parkingSlot = interfaceAdapter.getParkingSlotList()

        val response = interfaceAdapter.occupySlot(userId, slotId, stopEnd, parkingSlot)

        mongoClient.close()
        call.respond(response.first, response.second)
    }
    put("/parking-slot/{id}/increment-occupation") {

        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)

        val interfaceAdapter = InterfaceAdapter(collection)

        val slotId = call.parameters["id"]!!
        val newEndTime = call.receive<StopEnd>().stopEnd
        val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("email").asString()
        val parkingSlotList = interfaceAdapter.getParkingSlotList()
        val response = interfaceAdapter.incrementOccupation(userId, slotId, newEndTime, parkingSlotList)

        mongoClient.close()
        call.respond(response.first, response.second)
    }
    put("/parking-slot/{id}/free") {

        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)

        val interfaceAdapter = InterfaceAdapter(collection)

        val slotId = call.parameters["id"]!!
        val parkingSlotList = interfaceAdapter.getParkingSlotList()
        val response = interfaceAdapter.freeSlot(slotId, parkingSlotList)

        mongoClient.close()
        call.respond(response.first, response.second)

    }
    get("/parking-slots/{latitude}/{longitude}/{radius}") {

        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)

        val interfaceAdapter = InterfaceAdapter(collection)
        val latitude = call.parameters["latitude"]!!.toString().toDouble()
        val longitude = call.parameters["longitude"]!!.toString().toDouble()
        val radius = call.parameters["radius"]!!.toDouble()
        val center = Center(Position(latitude, longitude), radius)
        val response = interfaceAdapter.getAllParkingSlotsByRadius(center)

        mongoClient.close()
        call.respond(response)
    }
    get("/parking-slot/{id}") {

        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)

        val interfaceAdapter = InterfaceAdapter(collection)
        val slotId = call.parameters["id"]!!
        val response = interfaceAdapter.getParkingSlot(slotId)

        if (response.id == "") {
            mongoClient.close()
            val errorResponse = interfaceAdapter.createResponse(HttpStatusCode.NotFound, "errorCode", "ParkingSlotNotFound")
            call.respond(errorResponse.first, errorResponse.second)
        } else {
            mongoClient.close()
            call.respond(response)
        }
    }
    //END: parking-slot endpoints

}