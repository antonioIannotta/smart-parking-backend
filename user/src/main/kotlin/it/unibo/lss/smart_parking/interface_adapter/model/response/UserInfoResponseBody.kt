package it.unibo.lss.smart_parking.interface_adapter.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseBody(
    val errorCode: String?,
    val message: String,
    val email: String? = null,
    val name: String? = null
)
