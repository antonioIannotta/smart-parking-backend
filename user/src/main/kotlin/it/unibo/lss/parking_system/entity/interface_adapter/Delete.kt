package it.unibo.lss.parking_system.entity.interface_adapter

import it.unibo.lss.parking_system.entity.interface_adapter.model.ResponseCode
import it.unibo.lss.parking_system.entity.interface_adapter.model.response.ServerResponseBody
import it.unibo.lss.parking_system.use_cases.deleteUser
import it.unibo.lss.parking_system.use_cases.getUserInfo
import java.util.*

fun deleteExistingUser(email: String): ServerResponseBody {

    return if (Objects.isNull(getUserInfo(email)))
        ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
    else {
        deleteUser(email)
        ServerResponseBody(ResponseCode.SUCCESS.code, "success")
    }

}