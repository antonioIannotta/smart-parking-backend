package com.example.user.model.response

import com.example.user.model.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseBody(
    val code: String,
    val message: String,
    val userInfo: UserInfo? = null
)
