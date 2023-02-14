package com.example.utils

import com.example.user.utils.sendMail
import org.junit.Test

class EmailTest {

    /**
     * test successful authentication to google smtp server
     */
    @Test
    fun testAuthentication() {

        sendMail("test@test.it", "test", "test")

    }

}