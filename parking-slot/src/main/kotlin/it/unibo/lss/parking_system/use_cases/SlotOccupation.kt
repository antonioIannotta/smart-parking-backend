package it.unibo.lss.parking_system.use_cases

import kotlinx.serialization.Serializable

/**
 * This class represents a model for the occupation of a slot.
 */
@Serializable
class SlotOccupation(var slotId: String, var endStop: String)