package it.unibo.lss.smart_parking.framework.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import it.unibo.lss.smart_parking.interface_adapter.model.ResponseCode
import it.unibo.lss.smart_parking.interface_adapter.model.response.ServerResponseBody
import it.unibo.lss.smart_parking.interface_adapter.utils.getJWTVerifier

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
fun Application.configureAuthentication(tokenSecret: String) {

    install(Authentication) {

        jwt("auth-jwt") {
            realm = "Parking System Backend"
            verifier(getJWTVerifier(tokenSecret))
            validate { credential ->
                if (credential.payload.getClaim("userId").asString() != "")
                    JWTPrincipal(credential.payload)
                else
                    null
            }
            challenge { _, _ ->
                call.response.status(HttpStatusCode.Unauthorized)
                call.respond(ServerResponseBody(ResponseCode.UNAUTHORIZED.code, "Token is not valid or is expired"))
            }
        }

    }
}