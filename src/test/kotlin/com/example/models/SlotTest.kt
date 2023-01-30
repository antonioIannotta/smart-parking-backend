package com.example.models

import com.example.parkingSlot.models.Slot
import org.junit.Test
import kotlin.test.assertEquals

class SlotTest {

    @Test
    fun getSlotId() {
        val slot = Slot("A9")
        assertEquals("A8", slot.slotId)
    }
}