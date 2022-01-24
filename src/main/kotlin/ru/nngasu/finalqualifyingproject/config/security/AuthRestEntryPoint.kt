package ru.nngasu.finalqualifyingproject.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import ru.nngasu.finalqualifyingproject.exception.error.BaseError
import ru.nngasu.finalqualifyingproject.exception.error.RestError
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthRestEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val message = "Unauthorized user attempted to access the protected URL: " + request.requestURI
        val error = RestError(HttpStatus.FORBIDDEN, BaseError.ACCESS_DENIED, message, authException.localizedMessage)
        val mapper = ObjectMapper()
        val responseMsg = mapper.writeValueAsString(error)
        response.contentType = "application/json"
        response.writer.write(responseMsg)
    }

}
