package utils

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import interface_adapter.utils.generateJWT
import interface_adapter.utils.getJWTVerifier
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * test related to JWT util functions
 */
class JWTTest {

    private val testMail = "test@test.it"
    private val testSecret = "1234567890"

    @Test
    fun `test that token creation generate a jwt without throwing exceptions`() {

        generateJWT(testMail, testSecret)

    }

    @Test
    fun `test token validation`() {

        val jwt = generateJWT(testMail, testSecret)
        getJWTVerifier(testSecret).verify(jwt)

    }

    @Test
    fun `test that token validation on expired token throws an exception`() {

        val jwt = generateJWT(testMail, testSecret, -3_600_000)
        assertThrows<TokenExpiredException> {
            getJWTVerifier(testSecret).verify(jwt)
        }

    }

    @Test
    fun `test that token validation on not correctly formatted token throws an exception`() {

        assertThrows<JWTDecodeException> {
            getJWTVerifier(testSecret).verify("wrong-token")
        }

    }

}
