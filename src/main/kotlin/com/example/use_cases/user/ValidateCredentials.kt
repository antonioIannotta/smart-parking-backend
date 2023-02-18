package com.example.user.com.example.use_cases.user

import com.example.entity.user.UserCredentials
import com.mongodb.client.model.Filters

fun validateCredentials(userCredentials: UserCredentials): Boolean {

    val mongoClient = getMongoClient()
    val mongoCollection = getUserCollection(mongoClient)

    val filter = Filters.eq("email", userCredentials.mail)
    val userInfoDocument = mongoCollection.find(filter).first()

    mongoClient.close()

    return userInfoDocument["password"] == userCredentials.password

}