package it.unibo.lss.parking_system.entity

import kotlinx.serialization.Serializable

/**
 * This data class represents a model for the Parking slot to be used when either a parking slot or the full list of
 * parking slot is required by the client
 */
@Serializable
class ParkingSlot(val id: String, val occupied: Boolean, val endStop: String,
    val latitude: String, val longitude: String, val userId: String)
