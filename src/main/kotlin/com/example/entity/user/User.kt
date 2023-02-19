package com.example.entity.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val password: String,
    val name: String,
    val surname: String
)