package ru.nngasu.finalqualifyingproject.exception

import ru.nngasu.finalqualifyingproject.exception.error.UserError

class UserException(message: String?): Exception(message) {
    var error: UserError? = UserError.UNSUPPORTED_OPERATION

    constructor(message: String?, error: UserError): this(message){
        this.error = error
    }
}