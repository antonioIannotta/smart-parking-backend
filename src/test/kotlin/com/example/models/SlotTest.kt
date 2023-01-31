package com.example.models

import com.example.parkingSlot.models.SlotId
import org.junit.Test
import kotlin.test.assertEquals

class SlotTest {

    @Test
    fun getSlotId() {
        val slot = SlotId("A9")
        assertEquals("A8", slot.slotId)
    }
}