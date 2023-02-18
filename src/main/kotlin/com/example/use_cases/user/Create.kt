package com.example.user.com.example.use_cases.user

import org.bson.Document

fun createUser(mail: String, password: String, name: String, surname: String) {

    val mongoClient = getMongoClient()
    val mongoCollection = getUserCollection(mongoClient)

    val userDocument = Document()
        .append("email", mail)
        .append("password", password)
        .append("name", name)
        .append("surname", surname)
    mongoCollection.insertOne(userDocument)

    mongoClient.close()

}