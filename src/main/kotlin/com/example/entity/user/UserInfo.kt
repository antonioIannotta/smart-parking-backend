package com.example.entity.user

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val email: String,
    val name: String,
    val surname: String
)