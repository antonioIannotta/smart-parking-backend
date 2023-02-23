package it.unibo.lss.parking_system.use_cases

import it.unibo.lss.parking_system.entity.UserCredentials
import it.unibo.lss.parking_system.entity.UserInfo

interface UserUseCases {

    fun changePassword(userCredentials: UserCredentials)
    fun createUser(mail: String, password: String, name: String)
    fun deleteUser(mail: String)
    fun getUserInfo(mail: String): UserInfo?
    fun validateCredentials(mail: String, password: String): Boolean

}