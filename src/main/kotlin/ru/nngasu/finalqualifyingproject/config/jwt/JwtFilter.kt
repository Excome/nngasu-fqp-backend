package ru.nngasu.finalqualifyingproject.config.jwt

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils.hasText
import org.springframework.web.filter.GenericFilterBean
import ru.nngasu.finalqualifyingproject.model.User
import ru.nngasu.finalqualifyingproject.service.UserService
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


/**
@author Peshekhonov Maksim
 */
@Component
class JwtFilter : GenericFilterBean() {
    private val LOGGER: Logger = LogManager.getLogger(JwtFilter::class)
    private val AUTHORIZATION: String = "Authorization"

    @Autowired
    private lateinit var jwtProvider: JwtProvider
    @Autowired
    private lateinit var userService: UserService


    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse?, filterChain: FilterChain) {
        val token = getTokenFromRequest(servletRequest as HttpServletRequest)
        if (token != null && jwtProvider.validateToken(token)) {
            val userLogin = jwtProvider.getLoginFromToken(token)
            val user = userService.loadUserByUsername(userLogin)
            val auth = UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
//            LOGGER.debug("Generate token for ${user} user.")
            SecurityContextHolder.getContext().authentication = auth
        } else {
            LOGGER.warn("Token not valid!")
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearer = request.getHeader(AUTHORIZATION)
        return if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            bearer.substring(7)
        } else null
    }
}