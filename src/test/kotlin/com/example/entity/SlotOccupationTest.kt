package com.example.entity

import org.junit.Test
import use_cases.SlotOccupation
import java.time.LocalDateTime
import kotlin.test.assertEquals

class SlotOccupationTest {

    @Test
    fun getSlotOccupationId() {
        val slotOccupation = SlotOccupation("A9", LocalDateTime.now().toString())
        assertEquals("A9", slotOccupation.slotId)
    }
}