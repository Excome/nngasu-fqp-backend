package ru.nngasu.finalqualifyingproject.exception.error

/**
@author Peshekhonov Maksim
*/
enum class UserError(override val code: Int, override val message: String): Error {
    USER_NOT_FOUND(1001, "User not found"),
    USER_NOT_AUTHORIZED(1002, "User is not authorized"),
    EMAIL_IS_ALREADY_USED(1003,"User with this email is already registered"),
    USERNAME_IS_ALREADY_USED(1004,"User with this username is already registered"),
    PASSWORDS_ARE_NOT_EQUAL(1005, "Password and password confirm do not match"),
    UNSUPPORTED_OPERATION(1099, "Unsupported operation")
}