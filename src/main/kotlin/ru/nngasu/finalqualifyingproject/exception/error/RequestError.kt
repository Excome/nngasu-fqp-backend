package ru.nngasu.finalqualifyingproject.exception.error

/**
@author Peshekhonov Maksim
 */
enum class RequestError(override val code: Int, override val message: String) : Error {
    REQUEST_NOT_FOUND(1008, "Request with this id not found"),
    UNSUPPORTED_OPERATION(1099, "Unsupported operation")
}