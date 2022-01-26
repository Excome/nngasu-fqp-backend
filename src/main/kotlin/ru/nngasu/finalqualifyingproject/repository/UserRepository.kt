package ru.nngasu.finalqualifyingproject.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
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

}