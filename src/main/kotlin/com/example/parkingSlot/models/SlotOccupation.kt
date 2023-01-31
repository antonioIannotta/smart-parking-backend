package com.example.parkingSlot.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * This class represents a model for the occupation of a slot.
 */
@Serializable
data class SlotOccupation(val slotId: String, val endStop: String)
