package utils

import interface_adapter.utils.sendMail
import org.junit.jupiter.api.Test

class EmailTest {

    @Test
    fun `test successful authentication to google smtp server`() {

        sendMail("test@test.it", "test", "test")

    }

}