package it.unibo.lss.parking_system.interface_adapter.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseBody(
    val errorCode: String?,
    val message: String,
    val email: String? = null,
    val name: String? = null
)
