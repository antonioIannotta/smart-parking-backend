package com.example.user.controller

import com.example.user.model.DBOperationResult
import com.example.user.model.UserInfo
import com.example.user.model.request.SignUpRequestBody
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.bson.Document

class UserController {

    private val mongoAddress = "mongodb+srv://testUser:testUser@cluster0.r3hsl.mongodb.net/?retryWrites=true&w=majority"
    private val databaseName = "test-db"
    private val userCollectionName = "user-collection"
    private val mongoClientURI = MongoClientURI(mongoAddress)

    fun signUp(signUpRequestBody: SignUpRequestBody): DBOperationResult{

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        //TODO: check if a user with this mail is already registered
//        var filter =
//        mongoCollection.find()

        //user registration
        val userDocument = Document()
            .append("email", signUpRequestBody.email)
            .append("password", signUpRequestBody.password)
            .append("name",signUpRequestBody.name)
            .append("surname", signUpRequestBody.surname)
        mongoCollection.insertOne(userDocument)

        return DBOperationResult(200, "User successfully registered")

    }

    fun login(userName: String, password: String) {}

    fun info() {}

}