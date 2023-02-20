package it.unibo.lss.parking_system.utils

import it.unibo.lss.parking_system.entity.interface_adapter.utils.sendMail
import org.junit.jupiter.api.Test

class EmailTest {

    @Test
    fun `test successful authentication to google smtp server`() {

        sendMail("test@test.it", "test", "test")

    }

}