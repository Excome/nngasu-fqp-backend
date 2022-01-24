package ru.nngasu.finalqualifyingproject.config.jwt

import io.jsonwebtoken.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


/**
@author Peshekhonov Maksim
 */
@Component
class JwtProvider {
    private val LOGGER: Logger = LogManager.getLogger(JwtProvider::class)

    @Value("$(jwt.secret)")
    private lateinit var jwtSecret: String

    fun generateToken(login: String?): String? {
        val date: Date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant())
        LOGGER.debug("Generate token for ${login} user")
        return Jwts.builder()
            .setSubject(login)
            .setExpiration(date)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            return true
        } catch (expEx: ExpiredJwtException) {
            LOGGER.warn("Token expired")
        } catch (unsEx: UnsupportedJwtException) {
            LOGGER.warn("Unsupported jwt")
        } catch (mjEx: MalformedJwtException) {
            LOGGER.warn("Malformed jwt")
        } catch (sEx: SignatureException) {
            LOGGER.warn("Invalid signature")
        } catch (e: Exception) {
            LOGGER.warn("invalid token")
        }
        return false
    }

    fun getLoginFromToken(token: String?): String? {
        val claims: Claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody()
        return claims.subject
    }
}