package it.unibo.lss.parking_system.use_cases

import com.mongodb.client.model.Filters
import it.unibo.lss.parking_system.entity.UserCredentials
import java.util.*

fun validateCredentials(userCredentials: UserCredentials): Boolean {

    val mongoClient = getMongoClient()
    val mongoCollection = getUserCollection(mongoClient)

    val filter = Filters.eq("email", userCredentials.email)
    val userInfoDocument = mongoCollection.find(filter).first()

    mongoClient.close()

    return if (Objects.isNull(userInfoDocument)) false
    else userInfoDocument["password"] == userCredentials.password

}