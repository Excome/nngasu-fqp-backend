package ru.nngasu.finalqualifyingproject.model

import org.springframework.security.core.GrantedAuthority

enum class Role(val id: Int, private val priority: Int): GrantedAuthority {
    /**
    @see MAX priority can be 1000!
    */
    ROLE_GUEST(0, 100),
    ROLE_USER(1, 200),
    ROLE_TEACHER(2, 400),
    ROLE_TECHNICIAN(3, 500),
    ROLE_MODERATOR(4, 700),
    ROLE_ADMIN(5, 1000);

    override fun getAuthority(): String {
        return this.priority.toString()
    }
}
