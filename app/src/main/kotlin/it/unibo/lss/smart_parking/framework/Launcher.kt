package it.unibo.lss.smart_parking.framework

import io.github.gzaccaroni.smartparking.app.BuildConfig

/**
 * Launches the application by reading config values from application.properties
 */
fun main() {
    launchApplication(
        parkingSlotDbConnectionString = BuildConfig.PARKING_SLOT_DB_CONNECTION_STRING,
        userDbConnectionString = BuildConfig.USER_DB_CONNECTION_STRING,
        hashingSecret = BuildConfig.HASHING_SECRET,
    )
}