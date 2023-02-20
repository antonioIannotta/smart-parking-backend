package it.unibo.lss.parking_system.interface_adapter.model.response

import it.unibo.lss.parking_system.entity.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseBody(
    val code: String,
    val message: String,
    val userInfo: UserInfo? = null
)
