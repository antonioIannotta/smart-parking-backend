package com.example.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
//    val id: String,
    val name: String,
    val surname: String,
    val mail: String,
    val password: String
)
