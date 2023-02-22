package it.unibo.lss.parking_system.interface_adapter.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequestBody(
    val currentPassword: String? = null,
    val newPassword: String
)
