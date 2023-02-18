package com.example.parkingSlot.entity

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * This class represents a model for the occupation of a slot.
 */
@Serializable
class SlotOccupation(var slotId: String, var endStop: String)
