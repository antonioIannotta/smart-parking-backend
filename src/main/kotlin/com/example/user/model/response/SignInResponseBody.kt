package com.example.user.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponseBody(
    val logged: Boolean,
    val message: String,
    val token: String? = null
)
