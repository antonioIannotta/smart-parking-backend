package it.unibo.lss.smart_parking.interface_adapter

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Updates
import it.unibo.lss.smart_parking.interface_adapter.model.ResponseCode
import it.unibo.lss.smart_parking.interface_adapter.model.request.LoginRequestBody
import it.unibo.lss.smart_parking.interface_adapter.model.request.SignUpRequestBody
import it.unibo.lss.smart_parking.interface_adapter.model.response.ServerResponseBody
import it.unibo.lss.smart_parking.interface_adapter.model.response.SigningResponseBody
import it.unibo.lss.smart_parking.interface_adapter.model.response.UserInfoResponseBody
import it.unibo.lss.smart_parking.interface_adapter.utils.generateJWT
import it.unibo.lss.smart_parking.interface_adapter.utils.getRecoverPasswordMailContent
import it.unibo.lss.smart_parking.interface_adapter.utils.sendMail
import it.unibo.lss.smart_parking.use_cases.UserUseCases
import org.bson.Document
import org.bson.types.ObjectId
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/*
Copyright (c) 2022-2023 Antonio Iannotta & Luca Bracchi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
data class UserInterfaceAdapter(
    private val collection: MongoCollection<Document>,
    private val passwordHashingSecret: String
) : UserUseCases {


    override fun signUp(requestBody: SignUpRequestBody, tokenSecret: String): SigningResponseBody {
        return if (!this.userExists(requestBody.email)) {
            val passwordSalt = generateRandomSalt()
            val passwordHash = generateHash(
                password = requestBody.password,
                salt = passwordSalt
            )
            val userDocument = Document()
                .append("email", requestBody.email)
                .append("password_hash", passwordHash)
                .append("password_salt", passwordSalt)
                .append("name", requestBody.name)
            collection.insertOne(userDocument)
            val userId = userDocument.getObjectId("_id").toString()
            val jwt = generateJWT(userId, tokenSecret)
            SigningResponseBody(token = jwt, userId = userId)
        } else SigningResponseBody(errorCode = ResponseCode.ALREADY_REGISTERED.code)
    }

    override fun login(requestBody: LoginRequestBody, tokenSecret: String): SigningResponseBody {
        val userId = findUserIdByEmail(requestBody.email)
        return if (userId != null && this.validateCredentials(userId = userId, password = requestBody.password)) {
            val jwt = generateJWT(userId, tokenSecret)
            SigningResponseBody(
                errorCode = null,
                token = jwt,
                userId = userId,
            )
        } else
            SigningResponseBody(
                errorCode = ResponseCode.WRONG_CREDENTIALS.code,
            )
    }

    override fun getUserInfo(userId: String): UserInfoResponseBody {
        //find info on the user
        val filter = Filters.eq("_id", ObjectId(userId))
        val project = Projections.include("email", "name")
        val userInfoDocument = collection.find(filter).projection(project).first()

        return if (userInfoDocument != null)
            UserInfoResponseBody(
                null,
                "success",
                userInfoDocument["email"].toString(),
                userInfoDocument["name"].toString()
            )
        else UserInfoResponseBody(ResponseCode.WRONG_CREDENTIALS.code, "User not found")
    }

    override fun changePassword(userId: String, newPassword: String, currentPassword: String): ServerResponseBody {
        val userEmail = findUserEmailById(userId)
        if(userEmail != null){
            //old password validation
            if (!this.validateCredentials(userId = userId, password = currentPassword))
                return ServerResponseBody(ResponseCode.WRONG_CREDENTIALS.code, "Wrong password")

            // Store new password
            val newPasswordSalt = generateRandomSalt()
            val newPasswordHash = generateHash(
                password = newPassword,
                salt = newPasswordSalt
            )
            val filter = Filters.eq("email", userEmail)
            val update = Updates.combine(
                Updates.set("password_hash", newPasswordHash),
                Updates.set("password_salt", newPasswordSalt),
            )
            collection.updateOne(filter, update)
            return ServerResponseBody(null, "success")

        } else
            return ServerResponseBody(ResponseCode.WRONG_CREDENTIALS.code, "User not found")

    }

    override fun deleteUser(userId: String): ServerResponseBody {
        val filter = Filters.eq("_id", ObjectId(userId))
        collection.deleteOne(filter)
        return ServerResponseBody(null, "success")
    }

    override fun validateCredentials(userId: String, password: String): Boolean {
        val filter = Filters.and(
            Filters.eq("_id", ObjectId(userId)),
        )
        val userInfoDocument = collection.find(filter).first()

        return if (userInfoDocument != null) {
            val passwordHash = userInfoDocument.getString("password_hash")
            val passwordSalt = userInfoDocument.getString("password_salt")

            val generatedHash = generateHash(
                password = password,
                salt = passwordSalt
            )
            generatedHash == passwordHash
        } else false
    }
    override fun userExists(mail: String): Boolean {
        val filter = Filters.eq("email", mail)
        val project = Projections.include("_id")
        val userInfoDocument = collection.find(filter).projection(project).first()
        return userInfoDocument != null
    }

    private fun findUserIdByEmail(email: String): String? {
        val filter = Filters.eq("email", email)
        val project = Projections.include("_id")
        val userInfoDocument = collection.find(filter).projection(project).first()
        return userInfoDocument?.getObjectId("_id")?.toString()
    }

    private fun findUserEmailById(id: String): String? {
        val filter = Filters.eq("_id", ObjectId(id))
        val project = Projections.include("email")
        val userInfoDocument = collection.find(filter).projection(project).first()
        return userInfoDocument?.getString("email")
    }


    private fun generateRandomSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt.toHexString()
    }
    private fun generateHash(password: String, salt: String): String {
        val combinedSalt = "$salt$passwordHashingSecret".toByteArray()
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), combinedSalt, ITERATIONS, KEY_LENGTH)
        val key: SecretKey = factory.generateSecret(spec)
        val hash: ByteArray = key.encoded
        return hash.toHexString()
    }
    private fun ByteArray.toHexString(): String =
        HexFormat.of().formatHex(this)

    companion object {
        private const val ALGORITHM = "PBKDF2WithHmacSHA512"
        private const val ITERATIONS = 120_000
        private const val KEY_LENGTH = 256
    }
}
