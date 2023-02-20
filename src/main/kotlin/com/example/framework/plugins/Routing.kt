package com.example.framework.plugins

import com.example.framework.routing.exposedUserRoutes
import com.example.framework.routing.protectedUserRoutes
import interface_adapter.InterfaceAdapter
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import use_cases.IncrementOccupation
import use_cases.SlotId
import use_cases.SlotOccupation

fun Application.configureRouting(tokenSecret: String) {

    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        authenticate("auth-jwt") {
            protectedUserRoutes()
        }
        exposedUserRoutes(tokenSecret)

    }
}
