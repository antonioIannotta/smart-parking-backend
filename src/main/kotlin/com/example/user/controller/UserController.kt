package com.example.user.controller

import com.example.user.model.ResponseCode
import com.example.user.model.UserInfo
import com.example.user.model.request.SignInRequestBody
import com.example.user.model.request.SignUpRequestBody
import com.example.user.model.response.ServerResponseBody
import com.example.user.model.response.SigningResponseBody
import com.example.user.model.response.UserInfoResponseBody
import com.example.user.utils.generateJWT
import com.example.user.utils.getRecoverPasswordMailContent
import com.example.user.utils.sendMail
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Updates
import org.bson.Document
import java.util.*

class UserController(
    mongoAddress: String,
    private val databaseName: String,
    private val userCollectionName: String,
    private val tokenSecret: String
) {

    private val mongoClientURI = MongoClientURI(mongoAddress)

    fun signUp(signUpRequestBody: SignUpRequestBody): SigningResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        val filter = Filters.eq("email", signUpRequestBody.email)
        val registeredUser = mongoCollection.find(filter).first()

        return if (!Objects.isNull(registeredUser)) {
            mongoClient.close()
            SigningResponseBody(ResponseCode.ALREADY_REGISTERED.code, "An user with that email is already registered")
        } else {

            //user registration
            val userDocument = Document()
                .append("email", signUpRequestBody.email)
                .append("password", signUpRequestBody.password)
                .append("name", signUpRequestBody.name)
                .append("surname", signUpRequestBody.surname)
            mongoCollection.insertOne(userDocument)
            mongoClient.close()

            val jwt = generateJWT(signUpRequestBody.email, tokenSecret)

            SigningResponseBody(ResponseCode.SUCCESS.code, "success", jwt)

        }

    }

    fun signIn(signInRequestBody: SignInRequestBody): SigningResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)
        val signInResponseBody: SigningResponseBody

        //check if user exists and if it exists checks if passwords match
        val filter = Filters.eq("email", signInRequestBody.email)
        val userToSignIn = mongoCollection.find(filter).first()

        mongoClient.close()

        signInResponseBody =
                //check if a user was found
            if (Objects.isNull(userToSignIn))
                SigningResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
            //check if passwords match (if they match, return generate the jwt)
            else if (userToSignIn["password"] != signInRequestBody.password)
                SigningResponseBody(ResponseCode.PASSWORD_ERROR.code, "Wrong password")
            else {
                val token = generateJWT(signInRequestBody.email, tokenSecret)
                SigningResponseBody(ResponseCode.SUCCESS.code, "success", token)
            }

        return signInResponseBody
    }

    fun userInfo(email: String): UserInfoResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        val filter = Filters.eq("email", email)
        val project = Projections.exclude("password")
        val userInfoDocument = mongoCollection.find(filter).projection(project).first()
        val userInfo = this.createUserInfoFromDocument(userInfoDocument)

        mongoClient.close()

        return if (Objects.isNull(userInfo))
            UserInfoResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
        else
            UserInfoResponseBody(ResponseCode.SUCCESS.code, "success", userInfo)

    }

    /**
     * user deletion made deactivating the user on the db instead of delete the document
     */
    fun deleteUser(email: String): ServerResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        val filter = Filters.eq("email", email)
        val deletedUserDocument = mongoCollection.findOneAndDelete(filter)

        mongoClient.close()

        return if (Objects.isNull(deletedUserDocument))
            ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
        else
            ServerResponseBody(ResponseCode.SUCCESS.code, "success")

    }

    fun recoverPassword(email: String): ServerResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        //check the user exist and is active
        val filter = Filters.eq("email", email)
        val project = Projections.exclude("password")
        val userInfoDocument = mongoCollection.find(filter).projection(project).first()
        val userInfo = this.createUserInfoFromDocument(userInfoDocument)

        mongoClient.close()

        return if (Objects.isNull(userInfo))
            ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
        else {
            val jwt = generateJWT(userInfo.email, tokenSecret)
            val mailSubject = "Richiesta di cambio password"
            sendMail(userInfo.email, mailSubject, getRecoverPasswordMailContent(jwt))
            ServerResponseBody(ResponseCode.SUCCESS.code, "success")
        }

    }

    fun changePassword(email: String, newPassword: String, oldPassword: String?): ServerResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        val filter = Filters.eq("email", email)
        val project = Projections.exclude("password")
        val userInfoDocument = mongoCollection.find(filter).projection(project).first()
        val userInfo = this.createUserInfoFromDocument(userInfoDocument)

        //check the user exist and is active
        if (Objects.isNull(userInfo)) {
            mongoClient.close()
            return ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
        } else if (!Objects.isNull(oldPassword)) {
            //password validation
            val passwordProject = Projections.include("password")
            val result = mongoCollection.find(filter).projection(passwordProject).first()
            if (result["password"] != oldPassword) {
                mongoClient.close()
                return ServerResponseBody(ResponseCode.PASSWORD_ERROR.code, "Old password doesn't match")
            }
        }

        //change password
        val update = Updates.set("password", newPassword)
        mongoCollection.updateOne(filter, update)
        mongoClient.close()

        return ServerResponseBody(ResponseCode.SUCCESS.code, "success")
    }

    /**
     * cast a document retrieved from mongodb to an instance of UserInfo class
     */
    private fun createUserInfoFromDocument(document: Document): UserInfo =
        UserInfo(
            document["email"].toString(),
            document["name"].toString(),
            document["surname"].toString(),
        )

}