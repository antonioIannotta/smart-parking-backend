package it.unibo.lss.parking_system.interface_adapter

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Updates
import it.unibo.lss.parking_system.entity.UserCredentials
import it.unibo.lss.parking_system.interface_adapter.model.ResponseCode
import it.unibo.lss.parking_system.interface_adapter.model.request.SignUpRequestBody
import it.unibo.lss.parking_system.interface_adapter.model.response.ServerResponseBody
import it.unibo.lss.parking_system.interface_adapter.model.response.SigningResponseBody
import it.unibo.lss.parking_system.interface_adapter.model.response.UserInfoResponseBody
import it.unibo.lss.parking_system.interface_adapter.utils.generateJWT
import it.unibo.lss.parking_system.interface_adapter.utils.getRecoverPasswordMailContent
import it.unibo.lss.parking_system.interface_adapter.utils.sendMail
import it.unibo.lss.parking_system.use_cases.UserUseCases
import org.bson.Document

data class UserInterfaceAdapter(val collection: MongoCollection<Document>) : UserUseCases {

    override fun login(credentials: UserCredentials, tokenSecret: String): SigningResponseBody {
        return if (!this.userExists(credentials.email))
            SigningResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
        else if (this.validateCredentials(credentials)) {
            val jwt = generateJWT(credentials.email, tokenSecret)
            SigningResponseBody(null, "success", jwt)
        } else
            return SigningResponseBody(ResponseCode.PASSWORD_ERROR.code, "Wrong password")
    }

    override fun createUser(signUpRequestBody: SignUpRequestBody, tokenSecret: String): SigningResponseBody {
        return if (!this.userExists(signUpRequestBody.email)) {
            val userDocument = Document()
                .append("email", signUpRequestBody.email)
                .append("password", signUpRequestBody.password)
                .append("name", signUpRequestBody.name)
            collection.insertOne(userDocument)
            val jwt = generateJWT(signUpRequestBody.email, tokenSecret)
            SigningResponseBody(null, "success", jwt)
        } else SigningResponseBody(
            ResponseCode.ALREADY_REGISTERED.code,
            "An user with that email is already registered"
        )
    }

    override fun recoverPassword(mail: String, tokenSecret: String): ServerResponseBody {
        return if (userExists(mail)) {
            val jwt = generateJWT(mail, tokenSecret)
            sendMail(mail, "Password recovery mail", getRecoverPasswordMailContent(jwt))
            ServerResponseBody(null, "success")
        } else ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
    }

    override fun getUserInfo(mail: String): UserInfoResponseBody {
        //find info on the user
        val filter = Filters.eq("email", mail)
        val project = Projections.exclude("password")
        val userInfoDocument = collection.find(filter).projection(project).first()

        return if (userInfoDocument != null)
            UserInfoResponseBody(
                null,
                "success",
                userInfoDocument["email"].toString(),
                userInfoDocument["name"].toString()
            )
        else UserInfoResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
    }

    override fun changePassword(mail: String, newPassword: String, currentPassword: String?): ServerResponseBody {
        if(this.userExists(mail)){
            if (currentPassword != null) {
                //old password validation
                val credentials = UserCredentials(mail, currentPassword)
                if (!this.validateCredentials(credentials))
                    return ServerResponseBody(ResponseCode.PASSWORD_ERROR.code, "Wrong password")
            }
            val filter = Filters.eq("email", mail)
            val update = Updates.set("password", newPassword)
            collection.findOneAndUpdate(filter, update)
            return ServerResponseBody(null, "success")

        } else
            return ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")

    }

    override fun deleteUser(mail: String): ServerResponseBody {
        return if (!this.userExists(mail))
            ServerResponseBody(ResponseCode.USER_NOT_FOUND.code, "User not found")
        else {
            val filter = Filters.eq("email", mail)
            collection.findOneAndDelete(filter)
            ServerResponseBody(null, "success")
        }
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

}
