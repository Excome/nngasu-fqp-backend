package ru.nngasu.finalqualifyingproject.exception.error

import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import ru.nngasu.finalqualifyingproject.model.jsonView.ErrorView


/**
@author Peshekhonov Maksim
 */
class RestError() {
    @JsonView(ErrorView.Info::class)
    var status: HttpStatus? = null
    @JsonView(ErrorView.Info::class)
    var subCode = 0
    @JsonView(ErrorView.Info::class)
    var message: String? = null
    @JsonView(ErrorView.Info::class)
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