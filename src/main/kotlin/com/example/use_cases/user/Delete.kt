package com.example.use_cases.user

import com.mongodb.client.model.Filters

fun deleteUser(mail: String) {

    val mongoClient = getMongoClient()
    val mongoCollection = getUserCollection(mongoClient)

    val filter = Filters.eq("email", mail)
    mongoCollection.findOneAndDelete(filter)
    mongoClient.close()

}