package ru.nngasu.finalqualifyingproject.model

import com.fasterxml.jackson.annotation.JsonView
import org.springframework.security.core.GrantedAuthority
import ru.nngasu.finalqualifyingproject.model.jsonView.RoleView

/**
@author Peshekhonov, Maksim
*/
enum class Role(val id: Int, private val priority: Int): GrantedAuthority {
    /**
    @see MAX priority can be 1000!
    */
    @JsonView(RoleView.Name::class)
    ROLE_GUEST(0, 100),
    @JsonView(RoleView.Name::class)
    ROLE_USER(1, 200),
    @JsonView(RoleView.Name::class)
    ROLE_TEACHER(2, 400),
    @JsonView(RoleView.Name::class)
    ROLE_TECHNICIAN(3, 500),
    @JsonView(RoleView.Name::class)
    ROLE_MODERATOR(4, 700),
    @JsonView(RoleView.Name::class)
    ROLE_ADMIN(5, 1000);

    override fun getAuthority(): String {
        return this.priority.toString()
    }
}
