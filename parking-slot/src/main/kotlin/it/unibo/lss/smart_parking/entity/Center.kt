package it.unibo.lss.smart_parking.entity

import kotlinx.serialization.Serializable

@Serializable
class Center(val position: Position, val radius: Double)