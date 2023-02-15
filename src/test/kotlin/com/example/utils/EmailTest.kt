package com.example.utils

import com.example.user.utils.sendMail
import org.junit.Test

class EmailTest {

    @Test
    fun `test successful authentication to google smtp server`() {

        sendMail("test@test.it", "test", "test")

    }

}