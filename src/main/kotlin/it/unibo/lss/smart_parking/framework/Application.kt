package it.unibo.lss.smart_parking.framework

import it.unibo.lss.smart_parking.framework.plugins.configureAuthentication
import it.unibo.lss.smart_parking.framework.plugins.configureHTTP
import it.unibo.lss.smart_parking.framework.plugins.configureSerialization
import it.unibo.lss.smart_parking.framework.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(tokenSecret: String = "dSgUkXp2s5v8y/B?E(H+MbQeThWmYq3t") {
    configureAuthentication(tokenSecret)
    configureSerialization()
    configureHTTP()
    configureRouting(tokenSecret)
}
