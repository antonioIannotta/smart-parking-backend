package com.example.use_cases.user

import com.example.entity.user.UserCredentials
import com.mongodb.client.model.Filters
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