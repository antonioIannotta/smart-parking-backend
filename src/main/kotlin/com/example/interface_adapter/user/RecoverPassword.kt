package com.example.interface_adapter.user

import com.example.interface_adapter.user.model.ResponseCode
import com.example.interface_adapter.user.model.response.ServerResponseBody
import com.example.interface_adapter.user.utils.generateJWT
import com.example.interface_adapter.user.utils.getRecoverPasswordMailContent
import com.example.interface_adapter.user.utils.sendMail
import com.example.use_cases.user.getUserInfo
import java.util.*

fun recoverPassword(email: String, tokenSecret: String): ServerResponseBody {

    return if (!Objects.isNull(getUserInfo(email))) {
        val jwt = generateJWT(email, tokenSecret)
        sendMail(
            email,
            "Password recovery mail",
            getRecoverPasswordMailContent(jwt)
        )
        ServerResponseBody(ResponseCode.SUCCESS.code, "success")
    } else ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")

}