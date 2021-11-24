package ru.nngasu.finalqualifyingproject.exception.error

/**
@author Peshekhonov Maksim
 */
enum class BaseError(override val code: Int, override val message: String): Error {
    ACCESS_DENIED(900, "User access is denied to this resource"),
    NOT_FOUND(904, "Not found handler for this mapping"),
    METHOD_ARGUMENTS_NOT_VALID(905, "Method arguments not valid"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED(910, "This HTTP method to this url not supported"),
    UNEXPECTED_SERVER_ERROR(990, "Unexpected server error")
}