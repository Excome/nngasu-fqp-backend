package ru.nngasu.finalqualifyingproject.config.security

import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher


/**
@author Peshekhonov Maksim
 */
class AuthFilterConfigurer<H: HttpSecurityBuilder<H>>(
    authFilter: AuthFilter?,
    defaultLoginProcessingUrl: String?
) :
    AbstractAuthenticationFilterConfigurer<H, AuthFilterConfigurer<H>, AuthFilter>(
        authFilter,
        defaultLoginProcessingUrl
    ) {
    override fun createLoginProcessingUrlMatcher(loginProcessingUrl: String?): RequestMatcher? {
        return AntPathRequestMatcher(loginProcessingUrl, "POST")
    }

    override fun loginProcessingUrl(loginProcessingUrl: String?): AuthFilterConfigurer<H>? {
        return super.loginProcessingUrl(loginProcessingUrl)
    }
}