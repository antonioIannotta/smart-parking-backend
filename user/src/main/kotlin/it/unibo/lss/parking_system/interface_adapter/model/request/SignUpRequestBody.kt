package it.unibo.lss.parking_system.interface_adapter.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestBody(
    val email: String,
    val password: String,
    val name: String,
    val surname: String
)
