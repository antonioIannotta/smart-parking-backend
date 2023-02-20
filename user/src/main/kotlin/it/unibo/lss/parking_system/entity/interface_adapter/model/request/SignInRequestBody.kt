package it.unibo.lss.parking_system.entity.interface_adapter.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequestBody(
    val email: String,
    val password: String
)
