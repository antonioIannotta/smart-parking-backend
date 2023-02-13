package com.example.user.utils

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail

fun sendRecoverMail(to: String, jwt: String) {

    val email = HtmlEmail()
    email.hostName = "smtp.googlemail.com" //live: smtp.live.com
    email.setSmtpPort(465)
    email.setAuthenticator(DefaultAuthenticator("team.parkingslot@gmail.com", "bvyjssfgkymtecue"))
    email.isSSLOnConnect = true
    email.setFrom("support.parkingslot@gmail.com")
    email.subject = "Richiesta di cambio password"

    //TODO: add real link to change password page
    val mailContent = """
        <h2>cambia password</h2>
        <br>
        <div>
            Per cambiare la pasword vai al link seguente:
            <a href="https://jwt.io/">LINK</a>
            <br>
            <div>token: $jwt</div>
        </div>
    """.trimIndent()

    email.setHtmlMsg(mailContent)
    email.addTo(to)
    email.send()

}
