package com.example.interface_adapter.user.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RecoverMailRequestBody(
    val email: String,
)
