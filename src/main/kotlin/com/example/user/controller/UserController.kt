package com.example.user.controller

import com.example.user.model.UserInfo
import com.example.user.model.request.SignInRequestBody
import com.example.user.model.request.SignUpRequestBody
import com.example.user.model.response.ServerResponseBody
import com.example.user.model.response.SigningResponseBody
import com.example.user.model.response.UserInfoResponseBody
import com.example.user.utils.generateJWT
import com.example.user.utils.sendRecoverMail
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Updates
import org.bson.Document
import java.util.*

class UserController {

    private val mongoAddress = "mongodb+srv://testUser:testUser@cluster0.r3hsl.mongodb.net/?retryWrites=true&w=majority"
    private val databaseName = "test-db"
    private val userCollectionName = "user-collection"
    private val mongoClientURI = MongoClientURI(mongoAddress)

    fun signUp(signUpRequestBody: SignUpRequestBody, tokenSecret: String): SigningResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        //TODO: check if a user with this mail is already registered
        val filter = Filters.eq("email", signUpRequestBody.email)
        val registeredUser = mongoCollection.find(filter).first()

        return if (!Objects.isNull(registeredUser)) {
            mongoClient.close()
            SigningResponseBody("alreadyRegistered", "An user with that email is already registered")
        } else {

            //user registration
            val userDocument = Document()
                .append("email", signUpRequestBody.email)
                .append("password", signUpRequestBody.password)
                .append("name", signUpRequestBody.name)
                .append("surname", signUpRequestBody.surname)
                .append("active", true)
            mongoCollection.insertOne(userDocument)
            mongoClient.close()

            val jwt = generateJWT(signUpRequestBody.email, tokenSecret)

            SigningResponseBody("success", "User successfully registered", jwt)

        }

    }

    fun signIn(signInRequestBody: SignInRequestBody, tokenSecret: String): SigningResponseBody {

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
                SigningResponseBody("userNotFoundError", "User not found")
            //check if user is active or not
            else if (!userToSignIn["active"].toString().toBoolean())
                SigningResponseBody("userDeletedError", "User was deleted")
            //check if passwords match (if they match, return generate the jwt)
            else if (userToSignIn["password"] != signInRequestBody.password)
                SigningResponseBody("passwordError", "Wrong password")
            else {
                val token = generateJWT(signInRequestBody.email, tokenSecret)
                SigningResponseBody("success", "User logged", token)
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
            UserInfoResponseBody("userNotFoundError", "User not found")
        else
            UserInfoResponseBody("success", "User info retrieved", userInfo)

    }

    /**
     * user deletion made deactivating the user on the db instead of delete the document
     */
    fun deleteUser(email: String): ServerResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        val filter = Filters.eq("email", email)
        val update = Updates.set("active", false)
        val deletedUserDocument = mongoCollection.findOneAndUpdate(filter, update)

        mongoClient.close()

        return if (Objects.isNull(deletedUserDocument))
            ServerResponseBody("userNotFoundError", "User not found")
        else
            ServerResponseBody("success", "User deleted successfully")

    }

    fun recoverPassword(email: String, tokenSecret: String): ServerResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        //check the user exist and is active
        val filter = Filters.eq("email", email)
        val project = Projections.exclude("password")
        val userInfoDocument = mongoCollection.find(filter).projection(project).first()
        val userInfo = this.createUserInfoFromDocument(userInfoDocument)

        mongoClient.close()

        return if (Objects.isNull(userInfo))
            ServerResponseBody("userNotFound", "Can't recover password for this user")
        else if (!userInfo.active)
            ServerResponseBody("userDeleted", "Can't recover password for a deleted user")
        else {
            val jwt = generateJWT(userInfo.email, tokenSecret)
            sendRecoverMail(userInfo.email, jwt)
            ServerResponseBody("success", "Recovery mail sent to the user")
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
            return ServerResponseBody("userNotFound", "Can't recover password for this user")
        } else if (!userInfo.active) {
            mongoClient.close()
            return ServerResponseBody("userDeleted", "Can't recover password for a deleted user")
        } else if (!Objects.isNull(oldPassword)) {
            //password validation
            val passwordProject = Projections.include("password")
            val result = mongoCollection.find(filter).projection(passwordProject).first()
            if (result["password"] != oldPassword) {
                mongoClient.close()
                return ServerResponseBody("passwordError", "Old password doesn't match")
            }
        }

        //change password
        val update = Updates.set("password", newPassword)
        mongoCollection.updateOne(filter, update)
        mongoClient.close()

        return ServerResponseBody("success", "Password successfully changed")
    }

    /**
     * cast a document retrieved from mongodb to an instance of UserInfo class
     */
    private fun createUserInfoFromDocument(document: Document): UserInfo =
        UserInfo(
            document["email"].toString(),
            document["name"].toString(),
            document["surname"].toString(),
            document["active"].toString().toBoolean(),
        )

}