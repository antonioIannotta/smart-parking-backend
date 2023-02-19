package interface_adapter

import interface_adapter.model.ResponseCode
import interface_adapter.model.response.ServerResponseBody
import use_cases.deleteUser
import use_cases.getUserInfo
import java.util.*

fun deleteExistingUser(email: String): ServerResponseBody {

    return if (Objects.isNull(getUserInfo(email)))
        ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
    else {
        deleteUser(email)
        ServerResponseBody(ResponseCode.SUCCESS.code, "success")
    }

}