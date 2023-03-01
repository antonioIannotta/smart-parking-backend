package it.unibo.lss.smart_parking.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val password: String,
    val name: String
)