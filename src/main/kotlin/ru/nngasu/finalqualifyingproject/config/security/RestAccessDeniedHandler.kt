package ru.nngasu.finalqualifyingproject.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import ru.nngasu.finalqualifyingproject.exception.error.BaseError
import ru.nngasu.finalqualifyingproject.exception.error.RestError
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
@author Peshekhonov Maksim
 */

@Component
class RestAccessDeniedHandler: AccessDeniedHandler {
    private val LOG: Logger = LogManager.getLogger(RestAccessDeniedHandler::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun handle(request: HttpServletRequest, response: HttpServletResponse, e: AccessDeniedException) {
        val auth: Authentication? = SecurityContextHolder.getContext().authentication
        var msg = ""
        if (auth != null) {
            msg = "User: " + auth.getName().toString() + " attempted to access the protected URL: " + request.requestURI
            LOG.warn(msg)
        }
        val error = RestError(HttpStatus.FORBIDDEN, BaseError.ACCESS_DENIED, msg, e.localizedMessage)
        val mapper = ObjectMapper()
        val responseMsg = mapper.writeValueAsString(error)
        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.writer.write(responseMsg)
    }
}