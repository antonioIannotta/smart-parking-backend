package com.example.parkingSlot.entity

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * This data class represents a model for the Parking slot to be used when either a parking slot or the full list of
 * parking slot is required by the client
 */
@Serializable
class ParkingSlot(var id: String, var occupied: Boolean, var endStop: String)
