package ru.nngasu.finalqualifyingproject.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.nngasu.finalqualifyingproject.model.Equipment

/**
@author Peshekhonov Maksim
 */
@Repository
interface EquipmentRepository: JpaRepository<Equipment, Long> {
    fun findEquipmentByName(name: String): Equipment?
    override fun findAll(pageable: Pageable): Page<Equipment>
    fun findAllByNameContains(name: String?, pageable: Pageable): Page<Equipment>
    fun findAllByType(type: String?, pageable: Pageable): Page<Equipment>
    @Modifying
    @Query("delete from request_equipment where equipment_id = :id", nativeQuery = true)
    fun removeEquipmentsFromRequests(id: Long)
}