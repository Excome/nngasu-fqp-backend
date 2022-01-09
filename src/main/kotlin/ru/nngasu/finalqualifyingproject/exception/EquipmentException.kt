package ru.nngasu.finalqualifyingproject.exception

import ru.nngasu.finalqualifyingproject.exception.error.EquipmentError

/**
@author Lalykin Kirill
 */
class EquipmentException(message: String?): Exception(message) {
    var error: EquipmentError? = EquipmentError.UNSUPPORTED_OPERATION

    constructor(message: String?, error: EquipmentError): this(message){
        this.error = error
    }
}