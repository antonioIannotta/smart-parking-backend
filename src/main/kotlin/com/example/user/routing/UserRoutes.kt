package com.example.user.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting() {

    get("/user/sign-up") {
        call.respondText("You called /user/sign-up")
    }

    get("/user/sign-in") {
        call.respondText("You called /user/sign-in")
    }

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