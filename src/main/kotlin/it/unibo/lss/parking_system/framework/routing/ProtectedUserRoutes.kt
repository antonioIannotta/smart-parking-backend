package it.unibo.lss.parking_system.framework.routing

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.lss.parking_system.entity.Center
import it.unibo.lss.parking_system.entity.StopEnd
import it.unibo.lss.parking_system.interface_adapter.InterfaceAdapter
import it.unibo.lss.parking_system.interface_adapter.changePassword
import it.unibo.lss.parking_system.interface_adapter.deleteExistingUser
import it.unibo.lss.parking_system.interface_adapter.model.request.ChangePasswordRequestBody
import it.unibo.lss.parking_system.interface_adapter.userInfo
import it.unibo.lss.parking_system.use_cases.IncrementOccupation
import it.unibo.lss.parking_system.use_cases.SlotOccupation
import java.util.*

val mongoAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@cluster0.a3rz8ro.mongodb.net/?retryWrites=true"
val databaseName = "ParkingSystem"
val collectionName = "parking-slot"
fun Route.protectedUserRoutes() {

    //BEGIN: user endpoints
    get("/user/info") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()

        val responseBody = userInfo(userMail)

        if (Objects.isNull(responseBody.email))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    get("/user/delete") {
        val principal = call.principal<JWTPrincipal>()
        val userMail = principal!!.payload.getClaim("email").asString()

        val responseBody = deleteExistingUser(userMail)

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

        val responseBody = changePassword(userMail, requestBody.newPassword, requestBody.oldPassword)

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

        val slotId = call.parameters["id"]!!
        val endTime = call.receive<StopEnd>().stopEnd
        val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("email").asString()
        val response = InterfaceAdapter.occupy(userId, slotId, endTime, collection)

        mongoClient.close()
        call.respond(response.first, response.second)
    }
    put("/parking-slot/{id}/increment-occupation") {

        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)

        val slotId = call.parameters["id"]!!
        val newEndTime = call.receive<StopEnd>().stopEnd
        val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("email").asString()
        val response = InterfaceAdapter.incrementOccupation(userId, slotId, newEndTime, collection)

        mongoClient.close()
        call.respond(response.first, response.second)
    }
    put("/parking-slot/{id}/free") {

        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)

        val slotId = call.parameters["id"]!!
        val response = InterfaceAdapter.free(slotId, collection)

        mongoClient.close()
        call.respond(response.first, response.second)

    }
    get("/parking-slots/") {

        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)
        val center = call.receive<Center>()
        val response = InterfaceAdapter.getParkingSlotList(center, collection)

        mongoClient.close()
        call.respond(response)
    }
    get("/parking-slot/{id}") {

        val mongoClient = MongoClient(MongoClientURI(mongoAddress))
        val collection = mongoClient.getDatabase(databaseName).getCollection(collectionName)
        val slotId = call.parameters["id"]!!
        val response = InterfaceAdapter.getParkingSlot(slotId, collection)

        mongoClient.close()
        call.respond(response)
    }
    //END: parking-slot endpoints

}