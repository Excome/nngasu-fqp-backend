package ru.nngasu.finalqualifyingproject.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.nngasu.finalqualifyingproject.model.Request

/**
@author Peshekhonov Maksim
 */
@Repository
interface RequestRepository: JpaRepository<Request, Long> {
    override fun findAll(pageable: Pageable): Page<Request>
    fun findAllByAuthorUserName(userName: String, pageable: Pageable): Page<Request>
    fun findAllByResponsibleUserName(userName: String, pageable: Pageable): Page<Request>
    fun findAllByStatus(status: Boolean, pageable: Pageable): Page<Request>
}