package com.example.interface_adapter.user.model.response

import com.example.entity.user.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseBody(
    val code: String,
    val message: String,
    val userInfo: UserInfo? = null
)
