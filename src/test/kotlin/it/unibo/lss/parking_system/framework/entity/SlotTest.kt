package it.unibo.lss.parking_system.framework.entity

import org.junit.Test
import it.unibo.lss.parking_system.entity.use_cases.SlotId
import kotlin.test.assertEquals

class SlotTest {

    @Test
    fun getSlotId() {
        val slot = SlotId("A9")
        assertEquals("A8", slot.slotId)
    }
}