package it.unibo.lss.parking_system.use_cases

import org.bson.Document

fun createUser(mail: String, password: String, name: String) {

    val mongoClient = getMongoClient()
    val mongoCollection = getUserCollection(mongoClient)

    val userDocument = Document()
        .append("email", mail)
        .append("password", password)
        .append("name", name)
    mongoCollection.insertOne(userDocument)

    mongoClient.close()

}