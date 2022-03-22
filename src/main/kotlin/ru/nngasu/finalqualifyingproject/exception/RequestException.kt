package ru.nngasu.finalqualifyingproject.exception

import ru.nngasu.finalqualifyingproject.exception.error.RequestError

/**
 */
class RequestException(message: String?) : Exception(message) {
    var error: RequestError? = RequestError.UNSUPPORTED_OPERATION

    constructor(message: String?, error: RequestError): this(message){
        this.error = error
    }
}