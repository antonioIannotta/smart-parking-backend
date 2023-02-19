package interface_adapter.model.response

import entity.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseBody(
    val code: String,
    val message: String,
    val userInfo: UserInfo? = null
)
