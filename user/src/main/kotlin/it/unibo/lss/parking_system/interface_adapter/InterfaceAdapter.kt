package it.unibo.lss.parking_system.interface_adapter

import com.mongodb.client.MongoCollection
import it.unibo.lss.parking_system.entity.UserCredentials
import it.unibo.lss.parking_system.entity.UserInfo
import it.unibo.lss.parking_system.use_cases.UserUseCases
import org.bson.Document

data class UserInterfaceAdapter(val collection: MongoCollection<Document>): UserUseCases {

    override fun changePassword(userCredentials: UserCredentials) {
        TODO("Not yet implemented")
    }

    override fun createUser(mail: String, password: String, name: String) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(mail: String) {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(mail: String): UserInfo? {
        TODO("Not yet implemented")
    }

    override fun validateCredentials(mail: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

}
