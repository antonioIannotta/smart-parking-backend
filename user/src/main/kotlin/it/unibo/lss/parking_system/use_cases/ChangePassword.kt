package it.unibo.lss.parking_system.use_cases

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates

fun changeUserPassword(email: String, newPassword: String) {

    val mongoClient = getMongoClient()
    val mongoCollection = getUserCollection(mongoClient)

    val filter = Filters.eq("email", email)
    val update = Updates.set("password", newPassword)
    mongoCollection.findOneAndUpdate(filter, update)

}