package com.example.user.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.protectedUserRoutes() {

    get("/user/{userId?}/info") {
        call.respondText("You called /user/{userId?}/info")
    }

    get("/user/{userId?}/parking-slot") {
        call.respondText("You called /user/{userId?}/parking-slot")
    }

    get("/user/{userId?}/logout") {
        call.respondText("You called /user/{userId?}/logout")
    }

    get("/user/{userId?}/delete") {
        call.respondText("You called /user/{userId?}/delete")
    }

}