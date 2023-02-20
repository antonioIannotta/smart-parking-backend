package it.unibo.lss.parking_system.interface_adapter

import it.unibo.lss.parking_system.interface_adapter.model.ResponseCode
import it.unibo.lss.parking_system.interface_adapter.model.response.UserInfoResponseBody
import it.unibo.lss.parking_system.use_cases.getUserInfo
import java.util.*

fun userInfo(email: String): UserInfoResponseBody {

    val userInfo = getUserInfo(email)

    return if (Objects.isNull(userInfo))
        UserInfoResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
    else UserInfoResponseBody(ResponseCode.SUCCESS.code, "success", userInfo?.email, userInfo?.name)

}