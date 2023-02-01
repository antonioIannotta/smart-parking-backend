package com.example.parkingSlot.models

import kotlinx.serialization.Serializable

/**
 * This class represents a model for the request of incrementing a stop in a given slot
 */
@Serializable
class IncrementOccupation(var slotId: String, var endStop: String)
