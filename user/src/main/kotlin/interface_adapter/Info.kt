package interface_adapter

import interface_adapter.model.ResponseCode
import interface_adapter.model.response.UserInfoResponseBody
import use_cases.getUserInfo
import java.util.*

fun userInfo(email: String): UserInfoResponseBody {

    val userInfo = getUserInfo(email)

    return if (Objects.isNull(userInfo))
        UserInfoResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
    else UserInfoResponseBody(ResponseCode.SUCCESS.code, "success", userInfo)

}