package com.example.entity.user

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val mail: String,
    val password: String
)
