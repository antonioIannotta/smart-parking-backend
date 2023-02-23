package it.unibo.lss.parking_system.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val email: String,
    val password: String
)
