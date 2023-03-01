package it.unibo.lss.smart_parking.interface_adapter.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestBody(
    val email: String,
    val password: String,
    val name: String
)
