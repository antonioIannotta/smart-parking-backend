package it.unibo.lss.smart_parking.interface_adapter.utils

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail

/*
Copyright (c) 2022-2023 Antonio Iannotta & Luca Bracchi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
fun sendMail(
    to: String,
    subject: String,
    content: String,
    userMail: String = "team.parkingslot@gmail.com",
    userPassword: String = "tbfsdvotkmrhzard",
    host: String = "smtp.googlemail.com",
    port: Int = 465,
) {

    val email = HtmlEmail()
    email.hostName = host
    email.setSmtpPort(port)
    email.setAuthenticator(DefaultAuthenticator(userMail, userPassword))
    email.isSSLOnConnect = true
    email.setFrom(userMail)
    email.subject = subject
    email.setHtmlMsg(content)
    email.addTo(to)
    email.send()

}

//TODO: add real link to change password page
fun getRecoverPasswordMailContent(jwt: String): String = """
        <h2>Change password</h2>
        <br>
        <div>
            To change your password go to the following link:
            <a href="https://jwt.io/">LINK</a>
            <br>
            <div>token: $jwt</div>
        </div>
    """.trimIndent()