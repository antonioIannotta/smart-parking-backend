package it.unibo.lss.parking_system.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val email: String,
    val name: String
)