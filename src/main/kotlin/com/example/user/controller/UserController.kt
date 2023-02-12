package com.example.user.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.user.model.DBOperationResult
import com.example.user.model.UserInfo
import com.example.user.model.request.SignInRequestBody
import com.example.user.model.request.SignUpRequestBody
import com.example.user.model.response.SignInResponseBody
import com.example.user.model.response.UserInfoResponseBody
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Updates
import io.ktor.client.*
import io.ktor.http.*
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail
import org.bson.Document
import java.util.*

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
            .append("active", true)
        mongoCollection.insertOne(userDocument)

        mongoClient.close()

        return DBOperationResult(200, "User successfully registered")

    }

    fun signIn(signInRequestBody: SignInRequestBody, tokenSecret: String): SignInResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)
        val signInResponseBody: SignInResponseBody

        //check if user exists and if it exists checks if passwords match
        val filter = Filters.eq("email", signInRequestBody.email)
        val userToSignIn = mongoCollection.find(filter).first()

        mongoClient.close()

        signInResponseBody =
            //check if a user was found
            if(Objects.isNull(userToSignIn))
                SignInResponseBody(false, "User not found")
            //check if user is active or not
            else if(!userToSignIn["active"].toString().toBoolean())
                SignInResponseBody(false, "User was deleted")
            //check if passwords match (if they match, return generate the jwt)
            else if(userToSignIn["password"] == signInRequestBody.password) {
                val token = this.generateJWT(signInRequestBody.email, tokenSecret)
                SignInResponseBody(true, "User logged", token)
            } else
                SignInResponseBody(false, "Wrong password")

        return signInResponseBody
    }

    fun userInfo(email: String) : UserInfoResponseBody {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        val filter = Filters.eq("email", email)
        val project = Projections.exclude("password")
        val userInfoDocument = mongoCollection.find(filter).projection(project).first()
        val userInfo = this.createUserInfoFromDocument(userInfoDocument)

        mongoClient.close()

        return if(Objects.isNull(userInfo))
            UserInfoResponseBody(false, "User not found")
        else
            UserInfoResponseBody(true, "User info retrieved", userInfo)

    }

    /**
     * user deletion made deactivating the user on the db instead of delete the document
     */
    fun deleteUser(email: String): DBOperationResult {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        val filter = Filters.eq("email", email)
        val update = Updates.set("active", false)
        val deletedUserDocument = mongoCollection.findOneAndUpdate(filter, update)

        mongoClient.close()

        return if(Objects.isNull(deletedUserDocument))
            DBOperationResult(400, "User not found")
        else
            DBOperationResult(200, "User deleted successfully")

    }

    fun recoverPassword(email: String, tokenSecret: String): HttpStatusCode {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        //check the user exist and is active
        val filter = Filters.eq("email", email)
        val project = Projections.exclude("password")
        val userInfoDocument = mongoCollection.find(filter).projection(project).first()
        val userInfo = this.createUserInfoFromDocument(userInfoDocument)

        mongoClient.close()

        return if(Objects.isNull(userInfo) or !userInfo.active)
            HttpStatusCode(400, "Can't recover password for this user")
        else {
            val jwt = this.generateJWT(userInfo.email, tokenSecret)
            this.sendRecoverMail(userInfo.email, jwt)
            HttpStatusCode(200, "Recovery mail sent to the user")
        }

    }

    fun changePassword(email: String, newPassword: String, oldPassword: String?): HttpStatusCode {

        val mongoClient = MongoClient(mongoClientURI)
        val mongoCollection = mongoClient.getDatabase(databaseName).getCollection(userCollectionName)

        val filter = Filters.eq("email", email)
        val project = Projections.exclude("password")
        val userInfoDocument = mongoCollection.find(filter).projection(project).first()
        val userInfo = this.createUserInfoFromDocument(userInfoDocument)

        //check the user exist and is active
        if(Objects.isNull(userInfo) or !userInfo.active) {
            mongoClient.close()
            return HttpStatusCode(400, "Can't recover password for this user")
        } else if(!Objects.isNull(oldPassword)) {
            //password validation
            val passwordProject = Projections.include("password")
            val result = mongoCollection.find(filter).projection(passwordProject).first()
            if(result["password"] != oldPassword){
                mongoClient.close()
                return HttpStatusCode(400, "Old password doesn't match")
            }
        }

        //change password
        val update = Updates.set("password", newPassword)
        mongoCollection.updateOne(filter, update)
        mongoClient.close()

        return HttpStatusCode(200, "Password successfully changed")
    }

    /**
     * generates a jwt encrypted with HMAC256, valid for 2 hours
     */
    private fun generateJWT(email: String, tokenSecret: String): String {
        val expirationDate = Date(System.currentTimeMillis() + 7_200_000) //valid for 2 hours
        return JWT.create()
            .withAudience("Parking Client")
            .withIssuer("luca bracchi")
            .withClaim("email", email)
            .withExpiresAt(expirationDate)
            .sign(Algorithm.HMAC256(tokenSecret))
    }

    /**
     * cast a document retrieved from mongodb to an instance of UserInfo class
     */
    private fun createUserInfoFromDocument(document: Document) : UserInfo =
        UserInfo(
            document["email"].toString(),
            document["name"].toString(),
            document["surname"].toString(),
            document["active"].toString().toBoolean(),
        )

    private fun sendRecoverMail(to: String, jwt: String) {

        val email = SimpleEmail()
        email.hostName = "smtp.googlemail.com" //live: smtp.live.com
        email.setSmtpPort(465)
        email.setAuthenticator(DefaultAuthenticator("team.parkingslot@gmail.com", "bvyjssfgkymtecue"))
        email.isSSLOnConnect = true
        email.setFrom("support.parkingslot@gmail.com")
        email.subject = "Richiesta di cambio password"

        val mailContent = """
        cambia password
        vail al link: LINK
    """.trimIndent()

        email.setMsg(mailContent)
        email.addTo(to)
        email.send()

    }

}