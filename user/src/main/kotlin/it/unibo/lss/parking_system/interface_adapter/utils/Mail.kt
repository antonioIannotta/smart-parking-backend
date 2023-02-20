package it.unibo.lss.parking_system.interface_adapter.utils

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail

fun sendMail(
    to: String,
    subject: String,
    content: String,
    userMail: String = "team.parkingslot@gmail.com",
    userPassword: String = "ionrkvjoukjhtezf",
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