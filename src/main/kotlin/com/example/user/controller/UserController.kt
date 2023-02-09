package com.example.user.controller

import com.example.user.model.DBOperationResult
import com.example.user.model.UserInfo

class UserController {

    val mongoAddress = "mongodb+srv://testUser:testUser@cluster0.r3hsl.mongodb.net/?retryWrites=true&w=majority"
    val databaseName = "test-db"
    val userCollectionName = "user-collection"

    fun signUp(userInfo: UserInfo): DBOperationResult{



        return DBOperationResult(200, "ok")

    }

    fun login(userName: String, password: String) {}

    fun info() {}

}