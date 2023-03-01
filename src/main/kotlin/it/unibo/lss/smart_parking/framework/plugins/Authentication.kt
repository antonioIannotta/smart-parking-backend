package it.unibo.lss.smart_parking.framework.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import it.unibo.lss.smart_parking.interface_adapter.model.ResponseCode
import it.unibo.lss.smart_parking.interface_adapter.model.response.ServerResponseBody
import it.unibo.lss.smart_parking.interface_adapter.utils.getJWTVerifier

fun Application.configureAuthentication(tokenSecret: String) {

    install(Authentication) {

        jwt("auth-jwt") {
            realm = "Parking System Backend"
            verifier(getJWTVerifier(tokenSecret))
            validate { credential ->
                if (credential.payload.getClaim("email").asString() != "")
                    JWTPrincipal(credential.payload)
                else
                    null
            }
            challenge { _, _ ->
                call.response.status(HttpStatusCode.Unauthorized)
                call.respond(ServerResponseBody(ResponseCode.UNAUTHORIZED.code, "Token is not valid or has expired"))
            }
        }

    }
}