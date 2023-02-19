package com.example.interface_adapter.user

import com.example.interface_adapter.user.model.ResponseCode
import com.example.interface_adapter.user.model.response.ServerResponseBody
import com.example.use_cases.user.deleteUser
import com.example.use_cases.user.getUserInfo
import java.util.*

fun deleteExistingUser(email: String): ServerResponseBody {

    return if (Objects.isNull(getUserInfo(email)))
        ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
    else {
        deleteUser(email)
        ServerResponseBody(ResponseCode.SUCCESS.code, "success")
    }

}