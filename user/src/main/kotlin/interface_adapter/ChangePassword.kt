package interface_adapter

import com.example.entity.user.UserCredentials
import com.example.interface_adapter.user.model.ResponseCode
import com.example.interface_adapter.user.model.response.ServerResponseBody
import com.example.use_cases.user.changeUserPassword
import com.example.use_cases.user.getUserInfo
import com.example.use_cases.user.validateCredentials

fun changePassword(email: String, newPassword: String, oldPassword: String?): ServerResponseBody {

    if (getUserInfo(email) == null)
        return ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
    else if (oldPassword != null){
        //old password validation
        val credentials = UserCredentials(email, oldPassword)
        if (!validateCredentials(credentials))
            return ServerResponseBody(ResponseCode.PASSWORD_ERROR.code, "Wrong password")
    }

    changeUserPassword(email, newPassword)
    return ServerResponseBody(ResponseCode.SUCCESS.code, "success")
}