package it.unibo.lss.parking_system.framework.entity

import org.junit.Test
import it.unibo.lss.parking_system.entity.use_cases.SlotOccupation
import java.time.LocalDateTime
import kotlin.test.assertEquals

class SlotOccupationTest {

    @Test
    fun getSlotOccupationId() {
        val slotOccupation = SlotOccupation("A9", LocalDateTime.now().toString())
        assertEquals("A9", slotOccupation.slotId)
    }
}