package ru.nngasu.finalqualifyingproject.model

import org.springframework.security.core.GrantedAuthority

enum class Role(val id: Int, private val priority: Int): GrantedAuthority {
    /**
    @see MAX priority can be 1000!
    */
    GUEST(0, 100),
    USER(1, 200),
    STUDENT(2, 300),
    TEACHER(3, 400),
    MODERATOR(4, 500),
    ADMINISTRATOR(5, 600),
    DEVELOPER(6, 700),
    OWNER(7, 1000);

    override fun getAuthority(): String {
        return this.priority.toString()
    }
}
