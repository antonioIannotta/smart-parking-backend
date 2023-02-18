package com.example.interface_adapter.user

import com.example.interface_adapter.user.model.ResponseCode
import com.example.interface_adapter.user.model.response.UserInfoResponseBody
import com.example.use_cases.user.getUserInfo
import java.util.*

fun userInfo(mail: String): UserInfoResponseBody {

    val userInfo = getUserInfo(mail)

    return if (Objects.isNull(userInfo))
        UserInfoResponseBody(ResponseCode.SUCCESS.code, "success", userInfo)
    else UserInfoResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
}