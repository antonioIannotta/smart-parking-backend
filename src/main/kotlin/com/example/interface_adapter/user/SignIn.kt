package com.example.interface_adapter.user

import com.example.entity.user.UserCredentials
import com.example.interface_adapter.user.model.ResponseCode
import com.example.interface_adapter.user.model.response.SigningResponseBody
import com.example.interface_adapter.user.utils.generateJWT
import com.example.use_cases.user.validateCredentials
import java.util.*

fun signIn(credentials: UserCredentials, tokenSecret: String): SigningResponseBody {

    return if (Objects.isNull(userInfo(credentials.email)))
        SigningResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
    else if (validateCredentials(credentials)) {
        val jwt = generateJWT(credentials.email, tokenSecret)
        SigningResponseBody(ResponseCode.SUCCESS.code, "success", jwt)
    } else
        return SigningResponseBody(ResponseCode.PASSWORD_ERROR.code, "Wrong password")

}