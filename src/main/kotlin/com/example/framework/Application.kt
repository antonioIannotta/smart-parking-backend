package com.example.framework

import com.example.framework.plugins.configureAuthentication
import com.example.framework.plugins.configureHTTP
import com.example.framework.plugins.configureRouting
import com.example.framework.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    val tokenSecret = "dSgUkXp2s5v8y/B?E(H+MbQeThWmYq3t"

//    configureSecurity()
    configureAuthentication(tokenSecret)
    configureSerialization()
    configureHTTP()
    configureRouting(tokenSecret)
}
