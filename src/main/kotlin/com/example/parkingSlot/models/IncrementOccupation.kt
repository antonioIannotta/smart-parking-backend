package com.example.parkingSlot.models

import kotlinx.serialization.Serializable

@Serializable
data class IncrementOccupation(val slotId: String, val endStop: String)
