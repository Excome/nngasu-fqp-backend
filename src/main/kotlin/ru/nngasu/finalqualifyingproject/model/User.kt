package ru.nngasu.finalqualifyingproject.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.nngasu.finalqualifyingproject.model.jsonView.UserView
import java.util.*
import javax.persistence.*

/**
@author Peshekhonov Maksim
 */
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class User : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(UserView.Common::class)
    val id: Long = 0

    @JsonView(UserView.Common::class)
    var userName: String = ""
    var pass: String = ""

    @JsonView(UserView.Common::class)
    var email: String = ""

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    @JsonView(UserView.Common::class)
    var roles: MutableSet<Role> = mutableSetOf(Role.ROLE_GUEST)

    @JsonView(UserView.Common::class)
    var firstName: String = ""
    @JsonView(UserView.Common::class)
    var surName: String = ""

    @Transient
    var passConfirm: String = ""

    var verificationCode: String = ""
    @JsonView(UserView.Profile::class)
    var enabled: Boolean = true

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy HH:mm")
    @JsonView(UserView.Common::class)
    var createdDate: Date = Date()

    fun hasPriorityMoreThan(role: Role): Boolean {
        for (r in this.roles) {
            if (r.priority >= role.priority)
                return true
        }
        return false
    }

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
//        return this.enabled
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (userName != other.userName) return false
        if (pass != other.pass) return false
        if (email != other.email) return false
        if (roles != other.roles) return false
        if (firstName != other.firstName) return false
        if (surName != other.surName) return false
        if (verificationCode != other.verificationCode) return false
        if (enabled != other.enabled) return false
        if (createdDate != other.createdDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + pass.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + roles.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + surName.hashCode()
        result = 31 * result + verificationCode.hashCode()
        result = 31 * result + enabled.hashCode()
        result = 31 * result + createdDate.hashCode()
        return result
    }
}
