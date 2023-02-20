package it.unibo.lss.parking_system.interface_adapter.model.response

import kotlinx.serialization.Serializable

/**
 * send a token to authenticate the user
 * it's used for login/registration endpoint
 */
@Serializable
data class SigningResponseBody(
    val errorCode: String?,
    val message: String,
    val token: String? = null
)
