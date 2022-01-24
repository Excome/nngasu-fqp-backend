package ru.nngasu.finalqualifyingproject.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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

    fun createEquipment(name: String, type: String, count: Int, description: String): Equipment {
        var equipmentFromDb = equipmentRepository.findEquipmentByName(name = name)
        if (equipmentFromDb != null)
            throw EquipmentException("Equipment with name '${name}' already exist", EquipmentError.EQUIPMENT_IS_ALREADY_EXISTS)
        return equipmentRepository.save(Equipment(name = name, type = type, count = count, description = description))
    }

    fun getEquipmentByName(name: String): Equipment {
        return equipmentRepository.findEquipmentByName(name)
            ?: throw EquipmentException("Equipment '${name}' not found!", EquipmentError.EQUIPMENT_NOT_FOUND)
    }

    fun getEquipmentByType(type: String): Equipment {
        return equipmentRepository.findEquipmentByType(type)
            ?: throw EquipmentException("Equipment with type '${type} not found!'", EquipmentError.EQUIPMENT_NOT_FOUND)
    }
}