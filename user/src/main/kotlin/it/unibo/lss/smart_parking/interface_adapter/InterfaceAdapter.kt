package it.unibo.lss.smart_parking.interface_adapter

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Updates
import it.unibo.lss.smart_parking.entity.User
import it.unibo.lss.smart_parking.entity.UserCredentials
import it.unibo.lss.smart_parking.interface_adapter.model.ResponseCode
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
data class UserInterfaceAdapter(val collection: MongoCollection<Document>) : UserUseCases {

    override fun login(credentials: UserCredentials, tokenSecret: String): SigningResponseBody {
        val userId = findUserIdByEmail(credentials.email)
        return if (userId != null && this.validateCredentials(credentials)) {
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

    override fun createUser(signUpRequestBody: SignUpRequestBody, tokenSecret: String): SigningResponseBody {
        return if (!this.userExists(signUpRequestBody.email)) {
            val userDocument = Document()
                .append("email", signUpRequestBody.email)
                .append("password", signUpRequestBody.password)
                .append("name", signUpRequestBody.name)
            collection.insertOne(userDocument)
            val userId = userDocument.getObjectId("_id").toString()
            val jwt = generateJWT(userId, tokenSecret)
            SigningResponseBody(token = jwt, userId = userId)
        } else SigningResponseBody(errorCode = ResponseCode.ALREADY_REGISTERED.code)
    }

    override fun recoverPassword(mail: String, tokenSecret: String): ServerResponseBody {
        val userId = findUserIdByEmail(mail)
        return if (userId != null && userExists(mail)) {
            val jwt = generateJWT(userId, tokenSecret)
            sendMail(mail, "Password recovery mail", getRecoverPasswordMailContent(jwt))
            ServerResponseBody(null, "success")
        } else ServerResponseBody(ResponseCode.WRONG_CREDENTIALS.code, "User not found")
    }

    override fun getUserInfo(userId: String): UserInfoResponseBody {
        //find info on the user
        val filter = Filters.eq("_id", ObjectId(userId))
        val project = Projections.exclude("password")
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

    override fun changePassword(userId: String, newPassword: String, currentPassword: String?): ServerResponseBody {
        val userEmail = findUserEmailById(userId);
        if(userEmail != null){
            if (currentPassword != null) {
                //old password validation
                val credentials = UserCredentials(userEmail, currentPassword)
                if (!this.validateCredentials(credentials))
                    return ServerResponseBody(ResponseCode.WRONG_CREDENTIALS.code, "Wrong password")
            }
            val filter = Filters.eq("email", userEmail)
            val update = Updates.set("password", newPassword)
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

    override fun validateCredentials(credentials: UserCredentials): Boolean {
        val filter = Filters.eq("email", credentials.email)
        val userInfoDocument = collection.find(filter).first()

        return if (!this.userExists(credentials.email)) false
        else userInfoDocument["password"] == credentials.password
    }
    override fun userExists(mail: String): Boolean {
        val filter = Filters.eq("email", mail)
        val project = Projections.exclude("password")
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

}
