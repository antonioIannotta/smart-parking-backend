package com.example.parkingSlot.models

import kotlinx.serialization.Serializable

/**
 * This class represents a model for the Id of a single slot. This class is useful for the requests that are made by
 * the client
 */
@Serializable
data class SlotId(val slotId: String)
