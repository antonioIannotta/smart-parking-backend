package it.unibo.lss.parking_system.interface_adapter.model.response

import kotlinx.serialization.Serializable

/**
 * code: short string that identify that request outcome
 * message: description of the request outcome
 */
@Serializable
data class ServerResponseBody(
    val code: String,
    val message: String
)
