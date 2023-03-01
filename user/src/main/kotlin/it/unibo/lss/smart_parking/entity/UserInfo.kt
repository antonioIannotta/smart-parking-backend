package it.unibo.lss.smart_parking.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val email: String,
    val name: String
)