package com.example.parkingSlot.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class SlotOccupation(val slotId: String, val endStop: String)
