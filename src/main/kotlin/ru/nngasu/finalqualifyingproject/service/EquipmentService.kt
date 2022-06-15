package ru.nngasu.finalqualifyingproject.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.nngasu.finalqualifyingproject.exception.EquipmentException
import ru.nngasu.finalqualifyingproject.exception.error.EquipmentError
import ru.nngasu.finalqualifyingproject.model.Equipment
import ru.nngasu.finalqualifyingproject.repository.EquipmentRepository

/**
@author Lalykin Kirill
 */
@Service
class EquipmentService {
    @Autowired
    private lateinit var equipmentRepository: EquipmentRepository

    fun createEquipment(equipment: Equipment): Equipment {
        var equipmentFromDb = equipmentRepository.findEquipmentByName(name = equipment.name)
        if (equipmentFromDb != null)
            throw EquipmentException("Equipment with name '${equipment.name}' already exist", EquipmentError.EQUIPMENT_IS_ALREADY_EXISTS)

        return equipmentRepository.save(equipment)
    }


    fun getEquipmentByName(name: String): Equipment {
        return equipmentRepository.findEquipmentByName(name)
            ?: throw EquipmentException("Equipment with '${name}' name not found!", EquipmentError.EQUIPMENT_NOT_FOUND)
    }

    fun getEquipments(pageable: Pageable): List<Equipment>{
        val equipmentList = equipmentRepository.findAll(pageable).content

        if (equipmentList.isEmpty())
            throw EquipmentException("Equipments on page '${pageable.pageNumber}' " +
                    "not found!", EquipmentError.EQUIPMENT_NOT_FOUND)
        return equipmentList
    }

    fun getEquipmentsByName(name: String?, pageable: Pageable): List<Equipment>{
        val equipmentList = equipmentRepository.findAllByNameContains(name, pageable).content


        if (equipmentList.isEmpty())
            throw EquipmentException("Equipments which contains '${name}' " +
                    "on page '${pageable.pageNumber}' not found!", EquipmentError.EQUIPMENT_NOT_FOUND)
        return equipmentList
    }

    fun getEquipmentsByType(type: String, pageable: Pageable): List<Equipment>{
        val equipmentList = equipmentRepository.findAllByType(type, pageable).content

        if(equipmentList.isEmpty())
            throw EquipmentException("Equipments with type '${type} " +
                    "on page '${pageable.pageNumber}' not found!", EquipmentError.EQUIPMENT_NOT_FOUND)

        return equipmentList
    }

    fun changeEquipment(name: String, equipment: Equipment): Equipment {
        val equipmentFromDb = getEquipmentByName(name)

        if(name != equipment.name && equipmentFromDb.name == equipment.name)
            throw EquipmentException("Equipment with '${equipment.name} name already exist'", EquipmentError.EQUIPMENT_IS_ALREADY_EXISTS)

        equipmentFromDb.name = equipment.name
        equipmentFromDb.type = equipment.type
        equipmentFromDb.count = equipment.count
        equipmentFromDb.description = equipment.description

        return equipmentRepository.save(equipmentFromDb)
    }
    @Transactional
    fun removeEquipment(name: String){
        val equipment = getEquipmentByName(name)
        equipmentRepository.removeEquipmentsFromRequests(equipment.id)
        equipmentRepository.delete(equipment)
    }
}