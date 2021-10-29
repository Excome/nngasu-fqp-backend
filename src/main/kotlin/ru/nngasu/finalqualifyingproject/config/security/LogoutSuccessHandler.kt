package ru.nngasu.finalqualifyingproject.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
@author Peshekhonov Maksim
 */
@Component
class LogoutSuccessHandler: SimpleUrlLogoutSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onLogoutSuccess(request: HttpServletRequest?, response: HttpServletResponse, auth: Authentication?) {
        val responseBody: MutableMap<String, String?> = HashMap()
        val mapper = ObjectMapper()
        responseBody["auth"] = "LOGOUT"
        if (auth != null) {
            responseBody["username"] = auth.name
        } else {
            responseBody["username"] = null
        }
        response.status = HttpServletResponse.SC_OK
        response.contentType = "application/json"
        response.writer.write(mapper.writeValueAsString(responseBody))
    }
}