package ru.nngasu.finalqualifyingproject.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.nngasu.finalqualifyingproject.model.Equipment
import ru.nngasu.finalqualifyingproject.model.User

/**
@author Lalykin Kirill
 */
@Repository
interface EquipmentRepository: JpaRepository<Equipment, Long> {
    fun findEquipmentByName(name: String): Equipment?
    fun findEquipmentByType(type: String): Equipment?
}