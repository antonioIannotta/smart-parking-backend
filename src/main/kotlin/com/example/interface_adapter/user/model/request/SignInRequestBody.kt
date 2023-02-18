package com.example.interface_adapter.user.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequestBody(
    val email: String,
    val password: String
)
