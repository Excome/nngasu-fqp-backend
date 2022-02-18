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
data class User(
    @JsonView(UserView.Common::class)
    var userName: String,
    var pass: String,
    @JsonView(UserView.Common::class)
    var email: String,
): UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(UserView.Common::class)
    val id: Long = 0

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    @JsonView(UserView.Profile::class)
    var roles: MutableSet<Role> = mutableSetOf(Role.ROLE_GUEST)

    @JsonView(UserView.Profile::class)
    var firstName: String = ""
    @JsonView(UserView.Profile::class)
    var surName: String = ""

    @Transient
    var passConfirm: String = ""

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy HH:mm")
    @JsonView(UserView.Common::class)
    var createdDate: Date = Date()

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

   /* override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , userName = $userName , email = $email , roles = $roles , firstName = $firstName , surName = $surName , createdDate = $createdDate )"
    }*/
}
