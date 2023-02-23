package it.unibo.lss.parking_system.framework.routing

import it.unibo.lss.parking_system.entity.UserCredentials
import it.unibo.lss.parking_system.interface_adapter.model.request.RecoverMailRequestBody
import it.unibo.lss.parking_system.interface_adapter.model.request.SignUpRequestBody
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.lss.parking_system.framework.utils.getUserCollection
import it.unibo.lss.parking_system.interface_adapter.UserInterfaceAdapter
import java.util.*

fun Route.exposedUserRoutes(tokenSecret: String) {

    post("/user/sign-up") {

        //get parameter from request and create new user to register
        val signUpRequestBody = call.receive<SignUpRequestBody>()
        //register new user to db
        val interfaceAdapter = UserInterfaceAdapter(getUserCollection())
        val responseBody = interfaceAdapter.createUser(signUpRequestBody, tokenSecret)
        //sending response to client
        if (Objects.isNull(responseBody.token))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    post("/user/login") {

        //get parameter from request and create user to login
        val credentials = call.receive<UserCredentials>()
        //get jwt token and user info
        val interfaceAdapter = UserInterfaceAdapter(getUserCollection())
        val responseBody = interfaceAdapter.login(credentials, tokenSecret)
        //sending response to client
        if (Objects.isNull(responseBody.token))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    post("/user/recover-password") {

        val userMail = call.receive<RecoverMailRequestBody>().email

        val interfaceAdapter = UserInterfaceAdapter(getUserCollection())
        val responseBody = interfaceAdapter.recoverPassword(userMail, tokenSecret)
        //sending response to client
        if (responseBody.errorCode != null)
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

}