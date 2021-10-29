package ru.nngasu.finalqualifyingproject.exception

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.nngasu.finalqualifyingproject.exception.error.RestError


/**
@author Peshekhonov Maksim
 */

@RestControllerAdvice
class RestExceptionHandler(): ResponseEntityExceptionHandler() {
    private val logger: Logger = LogManager.getLogger(RestExceptionHandler::class.java)


    @ExceptionHandler(*[UserException::class])
    fun handleUserException(ex: UserException): ResponseEntity<Any> {
        val error = RestError(HttpStatus.CONFLICT, ex.error!!, ex.message!!, ex.javaClass.simpleName)
        logger.error(error)

        return ResponseEntity<Any>(error, HttpHeaders(), HttpStatus.CONFLICT)
    }


}