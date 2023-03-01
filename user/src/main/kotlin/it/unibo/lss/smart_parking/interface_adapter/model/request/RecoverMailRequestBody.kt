package it.unibo.lss.smart_parking.interface_adapter.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RecoverMailRequestBody(
    val email: String,
)
