package interface_adapter

import com.example.interface_adapter.user.model.ResponseCode
import com.example.interface_adapter.user.model.request.SignUpRequestBody
import com.example.interface_adapter.user.model.response.SigningResponseBody
import com.example.interface_adapter.user.utils.generateJWT
import com.example.use_cases.user.createUser
import com.example.use_cases.user.getUserInfo
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