package com.example.parkingSlot.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * This data class represents a model for the Parking slot to be used when either a parking slot or the full list of
 * parking slot is required by the client
 */
@Serializable
data class ParkingSlot(val id: String, val occupied: Boolean, val endStop: String)
