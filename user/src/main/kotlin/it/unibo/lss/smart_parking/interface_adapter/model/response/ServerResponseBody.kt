package it.unibo.lss.smart_parking.interface_adapter.model.response

import kotlinx.serialization.Serializable

/**
 * code: short string that identify that request outcome
 * message: description of the request outcome
 */
@Serializable
data class ServerResponseBody(
    val errorCode: String?,
    val message: String
)
