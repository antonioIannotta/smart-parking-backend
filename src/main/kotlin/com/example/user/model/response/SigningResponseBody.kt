package com.example.user.model.response

import kotlinx.serialization.Serializable

/**
 * send a token to authenticate the user
 * it's used for login/registration endpoint
 */
@Serializable
data class SigningResponseBody (
    val code: String,
    val message: String,
    val token: String? = null
)
