package com.example.user.model.request

data class SignUpRequestBody(
    val email: String,
    val password: String,
    val name: String,
    val surname: String
)
