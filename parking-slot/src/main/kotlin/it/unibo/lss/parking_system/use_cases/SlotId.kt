package it.unibo.lss.parking_system.use_cases

import kotlinx.serialization.Serializable

/**
 * This class represents a model for the Id of a single slot. This class is useful for the requests that are made by
 * the client
 */
@Serializable
class SlotId(var slotId: String)
