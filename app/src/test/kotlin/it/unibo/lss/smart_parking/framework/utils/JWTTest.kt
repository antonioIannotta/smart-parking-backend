package it.unibo.lss.smart_parking.framework.utils

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import it.unibo.lss.smart_parking.app.BuildConfig
import it.unibo.lss.smart_parking.user.interface_adapter.utils.generateJWT
import it.unibo.lss.smart_parking.user.interface_adapter.utils.getJWTVerifier
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * test related to JWT util functions
 */
class JWTTest {

    private val testId = ObjectId().toString()
    private val testSecret = BuildConfig.HASHING_SECRET

    @Test
    fun `test that token creation generate a jwt without throwing exceptions`() {

        generateJWT(testId, testSecret)

    }

    @Test
    fun `test token validation`() {

        val jwt = generateJWT(testId, testSecret)
        getJWTVerifier(testSecret).verify(jwt)

    }

    @Test
    fun `test that token validation on expired token throws an exception`() {

        val jwt = generateJWT(testId, testSecret, -3_600_000)
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
