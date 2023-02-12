package com.example.user.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val email: String,
    val name: String,
    val surname: String,
    val active: Boolean
)
