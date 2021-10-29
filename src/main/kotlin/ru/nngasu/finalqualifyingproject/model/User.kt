package ru.nngasu.finalqualifyingproject.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

/**
@author Peshekhonov Maksim
 */
@Entity
@Table(name = "users")
data class User(
    var userName: String,
    var pass: String,
    var email: String,
): UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = arrayOf(JoinColumn(name = "user_id")))
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<Role> = mutableSetOf(Role.ROLE_GUEST)

    var firstName: String = ""
    var surName: String = ""

//    @CreatedDate
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyy HH:mm")
//    lateinit var createdDate: Date

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roles
    }

    override fun getPassword(): String {
        return this.pass
    }

    override fun getUsername(): String {
        return this.userName
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
