package com.example.entity.user

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val mail: String,
    val password: String,
    val name: String,
    val surname: String
)