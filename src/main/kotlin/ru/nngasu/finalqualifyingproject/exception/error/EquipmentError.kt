package ru.nngasu.finalqualifyingproject.exception.error

/**
@author Lalykin Kirill
*/
enum class EquipmentError(override val code: Int, override val message: String): Error {
    EQUIPMENT_IS_ALREADY_EXISTS(1006, "Equipment with this name exists"),
    EQUIPMENT_NOT_FOUND(1007, "Equipment with this name not found"),
    UNSUPPORTED_OPERATION(1099, "Unsupported operation")
}