package ru.nngasu.finalqualifyingproject.exception.error

/**
@author Lalykin Kirill
*/
enum class EquipmentError(override val code: Int, override val message: String): Error {
    EQUIPMENT_EXISTS(1006, "Equipment with this name exists"),
    EQUIPMENT_BY_NAME_NOT_FOUND(1007, "Equipment with this name not found"),
    EQUIPMENT_BY_TYPE_NOT_FOUND(1008,"Equipment with this type not found"),
    UNSUPPORTED_OPERATION(1099, "Unsupported operation")
}