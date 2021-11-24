package ru.nngasu.finalqualifyingproject.exception.error

import org.springframework.http.HttpStatus


/**
@author Peshekhonov Maksim
 */
class RestError() {
    var status: HttpStatus? = null
    var subCode = 0
    var message: String? = null
    var errors: List<String>? = null

    constructor(status: HttpStatus, message: String, errors: List<String>) : this() {
        this.status = status
        this.message = message
        this.errors = errors
    }

    constructor(status: HttpStatus, message: String, error: String) : this() {
        this.status = status
        this.message = message
        errors = listOf(error)
    }

    constructor(status: HttpStatus, error: Error, message: String, errorMessage: String) : this() {
        this.status = status
        subCode = error.code
        this.message = message
        errors = listOf(errorMessage)
    }

    constructor(status: HttpStatus, error: Error, message: String, errors: List<String>) : this() {
        this.status = status
        subCode = error.code
        this.message = message
        this.errors = errors
    }

    override fun toString(): String {
        return "RestError{" +
                "status=" + status +
                ", subCode=" + subCode +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}'
    }
}