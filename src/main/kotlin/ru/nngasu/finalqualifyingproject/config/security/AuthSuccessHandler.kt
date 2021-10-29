package ru.nngasu.finalqualifyingproject.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
@author Peshekhonov Maksim
 */
@Component
class AuthSuccessHandler: SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val responseBody: HashMap<String, String> =  hashMapOf()
        val mapper = ObjectMapper()
        responseBody["auth"] = "SUCCESS"
        responseBody["username"] = authentication!!.name
        response!!.status = HttpServletResponse.SC_OK
        response.contentType = "application/json"
        response.writer.write(mapper.writeValueAsString(responseBody))
    }
}