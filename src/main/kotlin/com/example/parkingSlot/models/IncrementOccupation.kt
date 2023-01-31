package com.example.parkingSlot.models

import kotlinx.serialization.Serializable

/**
 * This class represents a model for the request of incrementing a stop in a given slot
 */
@Serializable
data class IncrementOccupation(val slotId: String, val endStop: String)
