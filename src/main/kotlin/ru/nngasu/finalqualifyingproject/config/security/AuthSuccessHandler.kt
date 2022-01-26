package ru.nngasu.finalqualifyingproject.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import ru.nngasu.finalqualifyingproject.config.jwt.JwtProvider
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
@author Peshekhonov Maksim
 */
@Component
class AuthSuccessHandler: SimpleUrlAuthenticationSuccessHandler() {
    @Autowired
    private lateinit var jwtProvider: JwtProvider
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val responseBody: HashMap<String, String> =  hashMapOf()
        val mapper = ObjectMapper()
        // Generate JWT token after success auth
        val token = jwtProvider.generateToken(authentication!!.name)
        responseBody["token"] = token!!
        response!!.status = HttpServletResponse.SC_OK
        response.contentType = "application/json"
        response.writer.write(mapper.writeValueAsString(responseBody))
    }
}