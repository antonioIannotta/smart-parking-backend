package it.unibo.lss.smart_parking.framework.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.unibo.lss.smart_parking.framework.utils.getUserCollection
import it.unibo.lss.smart_parking.framework.utils.getUserMongoClient
import it.unibo.lss.smart_parking.user.interface_adapter.UserInterfaceAdapter
import it.unibo.lss.smart_parking.user.interface_adapter.model.request.LoginRequestBody
import it.unibo.lss.smart_parking.user.interface_adapter.model.request.SignUpRequestBody
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
fun Route.exposedUserRoutes(
    tokenSecret: String,
    passwordHashingSecret: String
) {

    post("/user/sign-up") {

        //get parameter from request and create new user to register
        val requestBody = call.receive<SignUpRequestBody>()
        //register new user to db
        val responseBody = getUserMongoClient().use { mongoClient ->
            val interfaceAdapter = UserInterfaceAdapter(getUserCollection(mongoClient), passwordHashingSecret)
            interfaceAdapter.signUp(requestBody, tokenSecret)
        }
        //sending response to client
        if (Objects.isNull(responseBody.token))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

    post("/user/login") {

        //get parameter from request and create user to login
        val requestBody = call.receive<LoginRequestBody>()
        //get jwt token and user info
        val responseBody = getUserMongoClient().use { mongoClient ->
            val interfaceAdapter = UserInterfaceAdapter(getUserCollection(mongoClient), passwordHashingSecret)
            interfaceAdapter.login(requestBody, tokenSecret)
        }
        //sending response to client
        if (Objects.isNull(responseBody.token))
            call.response.status(HttpStatusCode.BadRequest)
        else
            call.response.status(HttpStatusCode.OK)
        call.respond(responseBody)
    }

}