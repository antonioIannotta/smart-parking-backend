package interface_adapter

import entity.UserCredentials
import interface_adapter.model.ResponseCode
import interface_adapter.model.response.ServerResponseBody
import use_cases.changeUserPassword
import use_cases.getUserInfo
import use_cases.validateCredentials


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