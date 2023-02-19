package interface_adapter

import entity.UserCredentials
import interface_adapter.model.ResponseCode
import interface_adapter.model.response.SigningResponseBody
import interface_adapter.utils.generateJWT
import use_cases.validateCredentials
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