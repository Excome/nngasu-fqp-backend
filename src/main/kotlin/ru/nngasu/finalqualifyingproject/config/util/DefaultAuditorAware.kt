package ru.nngasu.finalqualifyingproject.config.util

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*


/**
@author Peshekhonov Maksim
 */
@Component
class DefaultAuditorAware() : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        val loggedName = SecurityContextHolder.getContext().authentication.name
        return Optional.ofNullable(loggedName)
    }
}