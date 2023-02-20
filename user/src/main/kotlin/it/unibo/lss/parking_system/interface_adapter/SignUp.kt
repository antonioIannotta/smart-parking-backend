package it.unibo.lss.parking_system.interface_adapter

import it.unibo.lss.parking_system.interface_adapter.model.ResponseCode
import it.unibo.lss.parking_system.interface_adapter.model.request.SignUpRequestBody
import it.unibo.lss.parking_system.interface_adapter.model.response.SigningResponseBody
import it.unibo.lss.parking_system.interface_adapter.utils.generateJWT
import it.unibo.lss.parking_system.use_cases.createUser
import it.unibo.lss.parking_system.use_cases.getUserInfo
import java.util.*

fun signUp(signUpRequestBody: SignUpRequestBody, tokenSecret: String): SigningResponseBody {

    return if (Objects.isNull(getUserInfo(signUpRequestBody.email))) {
        createUser(
            signUpRequestBody.email,
            signUpRequestBody.password,
            signUpRequestBody.name
        )
        val jwt = generateJWT(signUpRequestBody.email, tokenSecret)
        SigningResponseBody(ResponseCode.SUCCESS.code, "success", jwt)

    } else SigningResponseBody(
        ResponseCode.ALREADY_REGISTERED.code,
        "An user with that email is already registered"
    )

}