package com.example.user.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.user.model.DBOperationResult
import com.example.user.model.UserInfo
import com.example.user.model.request.SignInRequestBody
import com.example.user.model.request.SignUpRequestBody
import com.example.user.model.response.SignInResponseBody
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.model.Filters
import org.bson.Document
import java.util.*

class UserController {

    private val mongoAddress = "mongodb+srv://testUser:testUser@cluster0.r3hsl.mongodb.net/?retryWrites=true&w=majority"
    private val databaseName = "test-db"
    private val userCollectionName = "user-collection"
    private val mongoClientURI = MongoClientURI(mongoAddress)
    val tokenSecret = "dSgUkXp2s5v8y/B?E(H+MbQeThWmYq3t"

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

    fun signIn(signInRequestBody: SignInRequestBody): SignInResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)
        val signInResponseBody: SignInResponseBody

        //check if user exists and if it exists checks if passwords match
        val filter = Filters.eq("email", signInRequestBody.email)
        val userToSignIn = mongoCollection.find(filter).first()

        signInResponseBody =
            //check if a user was found
            if(Objects.isNull(userToSignIn))
                SignInResponseBody(false, "User not found")
            else
                //check if passwords match (if they match, return generate the jwt)
                if(userToSignIn["password"] == signInRequestBody.password) {
                val token = this.generateJWT(signInRequestBody.email)
                SignInResponseBody(true, "User logged", token)
            } else
                SignInResponseBody(false, "Wrong password")

        return signInResponseBody
    }

    fun info() {}

    private fun generateJWT(email: String): String {

        val expirationDate = Date(System.currentTimeMillis() + 7_200_000) //valid for 2 hours

        return JWT.create()
            .withAudience("Parking Client")
            .withIssuer("luca bracchi")
            .withClaim("email", email)
            .withExpiresAt(expirationDate)
            .sign(Algorithm.HMAC256(tokenSecret))

    }

}