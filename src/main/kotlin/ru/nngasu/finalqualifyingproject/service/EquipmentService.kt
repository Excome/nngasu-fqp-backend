package ru.nngasu.finalqualifyingproject.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.nngasu.finalqualifyingproject.exception.UserException
import ru.nngasu.finalqualifyingproject.exception.error.UserError
import ru.nngasu.finalqualifyingproject.model.Equipment
import ru.nngasu.finalqualifyingproject.model.User
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
            throw UserException("Equipment with name '${name}' already exist", UserError.EMAIL_IS_ALREADY_USED) // с исключением не понятно немного, не разобрался. Поставил UserError пока что.
        equipmentFromDb = equipmentRepository.findEquipmentByType(type = type)
        if (equipmentFromDb != null)
            throw UserException("Type '${type}' already exist", UserError.USERNAME_IS_ALREADY_USED) // читай выше


        return equipmentRepository.save(Equipment(name = name, type = type, count = count, description = description))
    }
    fun getEquipmentByEquipmentName(name: String): Equipment {
        return equipmentRepository.findEquipmentByName(name)
            ?: throw UserException("Equipment '${name}' not found!", UserError.USER_NOT_FOUND) // Объясни или кинь ссылку на исключения, курс я так и не досмотрел. В процессе. Сам гуглю и делаю или пределываю по твоему шаблону.
    }

    fun getEquipmentByType(type: String): Equipment {
        return equipmentRepository.findEquipmentByType(type)
            ?: throw UserException("Equipment with type '${type} not found!'", UserError.USER_NOT_FOUND) // Ну ты понял
    }

    fun saveEquipment(equipment: Equipment): Equipment {
        return equipmentRepository.save(equipment)
    }
}