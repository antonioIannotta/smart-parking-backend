package it.unibo.lss.smart_parking.entity

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * This data class represents a model for the Parking slot to be used when either a parking slot or the full list of
 * parking slot is required by the client
 */
@Serializable
class ParkingSlot(val id: String, val occupied: Boolean, val stopEnd: String,
    val latitude: Double, val longitude: Double, val userId: String)
