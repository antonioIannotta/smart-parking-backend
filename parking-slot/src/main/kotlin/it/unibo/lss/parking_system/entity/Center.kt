package it.unibo.lss.parking_system.entity

import kotlinx.serialization.Serializable

@Serializable
class Center(val position: Position, val radius: Double)