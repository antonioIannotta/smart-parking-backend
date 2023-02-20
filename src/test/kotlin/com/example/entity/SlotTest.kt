package com.example.entity

import org.junit.Test
import use_cases.SlotId
import kotlin.test.assertEquals

class SlotTest {

    @Test
    fun getSlotId() {
        val slot = SlotId("A9")
        assertEquals("A8", slot.slotId)
    }
}