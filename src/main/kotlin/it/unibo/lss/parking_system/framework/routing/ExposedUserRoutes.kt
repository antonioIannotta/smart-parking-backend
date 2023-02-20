package it.unibo.lss.parking_system.framework.routing

import it.unibo.lss.parking_system.entity.UserCredentials
import it.unibo.lss.parking_system.interface_adapter.model.request.RecoverMailRequestBody
import it.unibo.lss.parking_system.interface_adapter.model.request.SignUpRequestBody
import it.unibo.lss.parking_system.interface_adapter.recoverPassword
import it.unibo.lss.parking_system.interface_adapter.signIn
import it.unibo.lss.parking_system.interface_adapter.signUp
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.exposedUserRoutes(tokenSecret: String) {

    post("/user/sign-up") {

        //get parameter from request and create new user to register
        val signUpRequestBody = call.receive<SignUpRequestBody>()
        //register new user to db
        val responseBody = signUp(signUpRequestBody, tokenSecret)
        //sending response to client
        if (Objects.isNull(responseBody.token))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    post("/user/sign-in") {

        //get parameter from request and create user to login
        val credentials = call.receive<UserCredentials>()
        //get jwt token and user info
        val responseBody = signIn(credentials, tokenSecret)
        //sending response to client
        if (Objects.isNull(responseBody.token))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    post("/user/recover-password") {

        val userMail = call.receive<RecoverMailRequestBody>().email
        val responseBody = recoverPassword(userMail, tokenSecret)
        //sending response to client
        if (responseBody.errorCode != null)
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

}