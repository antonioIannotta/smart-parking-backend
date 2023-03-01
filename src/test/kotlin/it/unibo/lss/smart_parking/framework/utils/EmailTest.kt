package it.unibo.lss.smart_parking.framework.utils

import it.unibo.lss.smart_parking.interface_adapter.utils.sendMail
import org.junit.jupiter.api.Test

class EmailTest {

    @Test
    fun `test successful authentication to google smtp server`() {

        sendMail("test@test.it", "test", "test")

    }

}