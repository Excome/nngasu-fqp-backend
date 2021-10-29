package ru.nngasu.finalqualifyingproject.config.security

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
@author Peshekhonov Maksim
 */
class AuthFilter: UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if (request != null) {
            if (!request.method.equals("POST")) {
                throw AuthenticationServiceException("Authentication method not supported: " + request.method);
            }
        }

        return try {
            val objectMapper = ObjectMapper()
            val data: Map<String?, Any?>? =
                objectMapper.readValue(request!!.inputStream, object : TypeReference<Map<String?, Any?>?>() {})
            request!!.setAttribute("data.login", data)
            var username = (data?.get(usernameParameter) ?: "") as String
            val password = (data?.get(passwordParameter) ?: "") as String
            username = username.trim { it <= ' ' }
            val authRequest = UsernamePasswordAuthenticationToken(username, password)

            // Allow subclasses to set the "details" property
            setDetails(request, authRequest)
            authenticationManager.authenticate(authRequest)
        } catch (e: IOException) {
            throw AuthenticationServiceException("Authentication method not supported: " + request!!.method, e)
        }
    }
}