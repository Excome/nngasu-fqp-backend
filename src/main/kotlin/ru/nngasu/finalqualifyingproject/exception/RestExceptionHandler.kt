package ru.nngasu.finalqualifyingproject.exception

import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.*
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.nngasu.finalqualifyingproject.exception.error.BaseError
import ru.nngasu.finalqualifyingproject.exception.error.RestError
import java.util.function.Consumer


/**
@author Peshekhonov Maksim
 */

@RestControllerAdvice
class RestExceptionHandler: ResponseEntityExceptionHandler() {
    private val LOGGER: Logger = LogManager.getLogger(RestExceptionHandler::class)

    @ExceptionHandler(*[UserException::class])
    fun handleUserException(ex: UserException): ResponseEntity<Any> {
        val error = RestError(HttpStatus.CONFLICT, ex.error!!, ex.message!!, ex.javaClass.simpleName)
        LOGGER.error(error)

        return ResponseEntity<Any>(error, HttpHeaders(), error.status!!)
    }

    @ExceptionHandler(*[EquipmentException::class])
    fun handleUserException(ex: EquipmentException): ResponseEntity<Any> {
        val error = RestError(HttpStatus.CONFLICT, ex.error!!, ex.message!!, ex.javaClass.simpleName)
        LOGGER.error(error)

        return ResponseEntity<Any>(error, HttpHeaders(), error.status!!)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors: MutableList<String> = ArrayList()
        for (error: FieldError in ex.bindingResult.fieldErrors) {
            errors.add(error.field + ": " + error.defaultMessage)
        }
        for (error: ObjectError in ex.bindingResult.globalErrors) {
            errors.add(error.objectName + ": " + error.defaultMessage)
        }
        val restError = RestError(HttpStatus.BAD_REQUEST, BaseError.METHOD_ARGUMENTS_NOT_VALID, ex.localizedMessage, errors)
        LOGGER.error(restError.toString() + ", " + ex.localizedMessage + " " + errors)
        return handleExceptionInternal(
            ex, restError, headers, restError.status!!, request
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatch(
        ex: MethodArgumentTypeMismatchException, request: WebRequest?
    ): ResponseEntity<*>? {
        val error = ex.name + " should be of type " + ex.requiredType!!.name
        val restError = RestError(HttpStatus.BAD_REQUEST, ex.localizedMessage, error)
        LOGGER.error(ex.localizedMessage + " " + error)
        return ResponseEntity<Any?>(
            restError, HttpHeaders(), restError.status!!
        )
    }

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val error = ex.parameterName + " parameter is missing"
        val restError = RestError(HttpStatus.BAD_REQUEST, ex.localizedMessage, error)
        LOGGER.error(ex.localizedMessage + " " + error)
        return ResponseEntity<Any>(
            restError, HttpHeaders(), restError.status!!
        )
    }

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val builder = StringBuilder()
        builder.append(ex.method)
        builder.append(
            " method is not supported for this request. Supported methods are "
        )
        ex.supportedHttpMethods!!.forEach(Consumer { t: HttpMethod -> builder.append(t.toString() + " ") })
        val restError = RestError(
            HttpStatus.METHOD_NOT_ALLOWED, BaseError.HTTP_REQUEST_METHOD_NOT_SUPPORTED,
            ex.localizedMessage, builder.toString()
        )
        LOGGER.error(restError.toString() + ", " + ex.localizedMessage + " " + builder.toString())
        return ResponseEntity<Any>(
            restError, HttpHeaders(), restError.status!!
        )
    }

    override fun handleNoHandlerFoundException(
        ex: NoHandlerFoundException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val error = "No handler found for " + ex.httpMethod + " " + ex.requestURL
        val restError = RestError(HttpStatus.NOT_FOUND, BaseError.NOT_FOUND, ex.localizedMessage, error)
        LOGGER.error(ex.localizedMessage + " " + error)
        return ResponseEntity<Any>(restError, HttpHeaders(), restError.status!!)
    }

    override fun handleHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val builder = StringBuilder()
        builder.append(ex.contentType)
        builder.append(" media type is not supported. Supported media types are ")
        ex.supportedMediaTypes.forEach(Consumer { t: MediaType -> builder.append("$t, ") })
        val error = RestError(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            ex.localizedMessage, builder.substring(0, builder.length - 2)
        )
        LOGGER.error(ex.localizedMessage + " " + builder.toString())
        return ResponseEntity<Any>(
            error, HttpHeaders(), error.status!!
        )
    }


    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception, request: WebRequest?): ResponseEntity<*>? {
        val error = RestError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            BaseError.UNEXPECTED_SERVER_ERROR,
            ex.localizedMessage,
            "Unexpected server error"
        )
        val exception = ex.localizedMessage + "\n" + ExceptionUtils.getStackTrace(ex)
        LOGGER.error("Unexpected server error {}, Msg: {}", error, exception)
        return ResponseEntity<Any>(
            error, HttpHeaders(), error.status!!
        )
    }
}
