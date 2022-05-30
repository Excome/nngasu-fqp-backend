package ru.nngasu.finalqualifyingproject.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.nngasu.finalqualifyingproject.model.Role
import ru.nngasu.finalqualifyingproject.model.User

/**
@author Peshekhonov Maksim
 */
@Repository
interface UserRepository: JpaRepository<User, Long>{
    fun findUserByUserName(userName: String): User?
    fun findUserByEmail(email: String): User?
    override fun findAll(pageable: Pageable): Page<User>
    fun findAllByUserNameContains(userName: String?, pageable: Pageable): Page<User>
    fun findUserByUserNameAndVerificationCode(userName: String, verificationCode: String): User?

    @Query("select users.* from users inner join user_roles ur on users.id = ur.user_id where ur.roles='ROLE_TECHNICIAN' or ur.roles='ROLE_MODERATOR' or ur.roles='ROLE_ADMIN' group by users.id;", nativeQuery = true)
    fun findResponsibleUsers(): MutableList<User>
}