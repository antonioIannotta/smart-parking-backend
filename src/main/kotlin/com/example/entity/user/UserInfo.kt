package com.example.entity.user

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val mail: String,
    val name: String,
    val surname: String
)