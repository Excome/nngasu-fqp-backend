package ru.nngasu.finalqualifyingproject.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.nngasu.finalqualifyingproject.model.Request

/**
@author Peshekhonov Maksim
 */
@Repository
interface RequestRepository: JpaRepository<Request, Long> {
}