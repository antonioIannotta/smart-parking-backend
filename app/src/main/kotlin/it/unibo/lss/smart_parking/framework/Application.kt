package it.unibo.lss.smart_parking.framework

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import it.unibo.lss.smart_parking.app.BuildConfig
import it.unibo.lss.smart_parking.framework.plugins.configureAuthentication
import it.unibo.lss.smart_parking.framework.plugins.configureHTTP
import it.unibo.lss.smart_parking.framework.plugins.configureRouting
import it.unibo.lss.smart_parking.framework.plugins.configureSerialization

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
fun main() {
    // Please change the following secrets in production.
    val hashingSecret = BuildConfig.HASHING_SECRET
    val userDbConnectionString = BuildConfig.USER_DB_CONNECTION_STRING
    val parkingSlotDbConnectionString = BuildConfig.PARKING_SLOT_DB_CONNECTION_STRING

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = {
        module(
            userDbConnectionString = userDbConnectionString,
            parkingSlotDbConnectionString = parkingSlotDbConnectionString,
            hashingSecret = hashingSecret,
        )
    })
        .start(wait = true)
}

fun Application.module(
    userDbConnectionString: String,
    parkingSlotDbConnectionString: String,
    hashingSecret: String,
) {
    configureAuthentication(
        tokenSecret = hashingSecret
    )
    configureSerialization()
    configureHTTP()
    configureRouting(
        hashingSecret = hashingSecret,
        userDbConnectionString = userDbConnectionString,
        parkingSlotDbConnectionString = parkingSlotDbConnectionString
    )
}
