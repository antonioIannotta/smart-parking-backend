package it.unibo.lss.parking_system.interface_adapter

import it.unibo.lss.parking_system.entity.UserCredentials
import it.unibo.lss.parking_system.interface_adapter.model.ResponseCode
import it.unibo.lss.parking_system.interface_adapter.model.response.ServerResponseBody
import it.unibo.lss.parking_system.use_cases.changeUserPassword
import it.unibo.lss.parking_system.use_cases.getUserInfo
import it.unibo.lss.parking_system.use_cases.validateCredentials


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
    return ServerResponseBody(null, "success")
}