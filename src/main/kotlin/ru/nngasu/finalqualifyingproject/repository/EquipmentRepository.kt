package ru.nngasu.finalqualifyingproject.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.nngasu.finalqualifyingproject.model.Equipment

/**
@author Peshekhonov Maksim
 */
@Repository
interface EquipmentRepository: JpaRepository<Equipment, Long> {
}