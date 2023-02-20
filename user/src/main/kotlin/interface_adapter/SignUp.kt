package interface_adapter

import interface_adapter.model.ResponseCode
import interface_adapter.model.request.SignUpRequestBody
import interface_adapter.model.response.SigningResponseBody
import interface_adapter.utils.generateJWT
import use_cases.createUser
import use_cases.getUserInfo
import java.util.*

fun signUp(signUpRequestBody: SignUpRequestBody, tokenSecret: String): SigningResponseBody {

    return if (Objects.isNull(getUserInfo(signUpRequestBody.email))) {
        createUser(
            signUpRequestBody.email,
            signUpRequestBody.password,
            signUpRequestBody.name,
            signUpRequestBody.surname
        )
        val jwt = generateJWT(signUpRequestBody.email, tokenSecret)
        SigningResponseBody(ResponseCode.SUCCESS.code, "success", jwt)

    } else SigningResponseBody(
        ResponseCode.ALREADY_REGISTERED.code,
        "An user with that email is already registered"
    )

}