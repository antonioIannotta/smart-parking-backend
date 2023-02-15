package com.example

import com.example.plugins.configureAuthentication
import com.example.plugins.configureHTTP
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
//    configureSecurity()
    configureSerialization()
    configureHTTP()
    configureRouting()
    configureAuthentication()
}
