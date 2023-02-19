package interface_adapter

import interface_adapter.model.ResponseCode
import interface_adapter.model.response.ServerResponseBody
import interface_adapter.utils.generateJWT
import interface_adapter.utils.getRecoverPasswordMailContent
import interface_adapter.utils.sendMail
import use_cases.getUserInfo
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