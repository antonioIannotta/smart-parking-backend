package com.example.models

import com.example.parkingSlot.models.SlotOccupation
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class SlotOccupationTest {

    @Test
    fun getSlotOccupationId() {
        val slotOccupation = SlotOccupation("A9", LocalDateTime.now().toString())
        assertEquals("A9", slotOccupation.slotId)
    }
}