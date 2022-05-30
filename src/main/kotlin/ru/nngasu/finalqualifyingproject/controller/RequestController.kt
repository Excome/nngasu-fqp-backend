package ru.nngasu.finalqualifyingproject.controller

import com.fasterxml.jackson.annotation.JsonView
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.nngasu.finalqualifyingproject.exception.RequestException
import ru.nngasu.finalqualifyingproject.model.Request
import ru.nngasu.finalqualifyingproject.model.Role
import ru.nngasu.finalqualifyingproject.model.User
import ru.nngasu.finalqualifyingproject.model.jsonView.RequestView
import ru.nngasu.finalqualifyingproject.service.RequestService

/**
 * @author Peshekhonov Maksim
 */
@RestController
class RequestController {
    private val LOGGER: Logger = LogManager.getLogger(RequestController::class)

    @Autowired
    lateinit var requestService: RequestService

    @GetMapping("/requests")
    @JsonView(RequestView.CommonView::class)
    @Throws(RequestException::class)
    fun getRequests(@RequestParam(required = false) author: String?,
                    @RequestParam(required = false) responsible: String?,
                    @PageableDefault(size = 10, sort = ["id"],
                        direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<List<Request>> {
        val responseBody =
            if (!author.isNullOrBlank() && responsible.isNullOrBlank()){
                LOGGER.info("Gonna get a list of requests by '$author' author name at '${pageable.pageNumber}' page")
                requestService.getRequestByAuthor(author, pageable)
            } else if (!responsible.isNullOrBlank() && author.isNullOrBlank()){
                LOGGER.info("Gonna get a list of requests by '$responsible' responsible name at '${pageable.pageNumber}' page")
                requestService.getRequestsByResponsible(responsible, pageable)
            } else {
                LOGGER.info("Gonna get a list of requests at'${pageable.pageNumber}' page")
                requestService.getRequests(pageable)
            }

        LOGGER.debug("Return list of equipments: $responseBody")
        return ResponseEntity(responseBody, HttpStatus.OK)
    }

    @GetMapping("/requests/{id}")
    @JsonView(RequestView.All::class)
    @Throws(RequestException::class)
    fun getRequest(@PathVariable id: Long,
                   @AuthenticationPrincipal crrUser: User): ResponseEntity<Request>{
        LOGGER.info("User ${crrUser.userName} tries to get request with '$id' id")
        return if (crrUser.hasPriorityMoreThan(Role.ROLE_TEACHER)){
            val responseBody = requestService.getRequestById(id = id)
            LOGGER.debug("Return request: $responseBody")
            ResponseEntity(responseBody, HttpStatus.OK)
        }else {
            LOGGER.warn("User '${crrUser.userName}' doesn't have permissions to get request with '$id' id")
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }

    @PostMapping("/requests")
    @JsonView(RequestView.All::class)
    @Throws(RequestException::class)
    fun createRequest(@RequestBody request: Request,
                      @AuthenticationPrincipal crrUser: User): ResponseEntity<Request>{

        LOGGER.info("User '${crrUser.userName}' tries to create new request: $request")
        return if (crrUser.hasPriorityMoreThan(Role.ROLE_TEACHER)) {
            val responseBody = requestService.createRequest(request)
            LOGGER.debug("Returned $responseBody")
            ResponseEntity(responseBody, HttpStatus.OK)
        } else {
            LOGGER.warn("User '${crrUser.userName}' doesn't have permissions to create $request")
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }

    @PutMapping("/requests/{id}")
    @JsonView(RequestView.All::class)
    @Throws(RequestException::class)
    fun editRequest(@PathVariable id: Long, @RequestBody request: Request,
                    @AuthenticationPrincipal crrUser: User): ResponseEntity<Request>{
        LOGGER.info("User '${crrUser.userName}' tries to edit request with '$id' id: $request")
        return if (crrUser.hasPriorityMoreThan(Role.ROLE_TECHNICIAN)){
            val responseBody = requestService.changeRequest(request)
            LOGGER.debug("Returned: $responseBody")
            ResponseEntity(responseBody, HttpStatus.OK)
        } else {
            LOGGER.warn("User '${crrUser.userName}' doesn't have permissions to edit request with '$id' id")
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }

    @PutMapping("/requests/{id}/assign")
    @JsonView(RequestView.All::class)
    @Throws(RequestException::class)
    fun assignRequest(@PathVariable id: Long, @RequestBody requestBody: Request,
                      @AuthenticationPrincipal crrUser: User): ResponseEntity<Request>{
        LOGGER.info("User '${crrUser.userName}' tries to assigt request with '$id' id to user ${requestBody.responsible}")
        return if (crrUser.hasPriorityMoreThan(Role.ROLE_TECHNICIAN)){
            val responseBody = requestService.assignRequest(id, requestBody)
            LOGGER.debug("User ${crrUser.userName} successful assign request with '$id' id, return: $responseBody")
            ResponseEntity(responseBody, HttpStatus.OK)
        } else {
            LOGGER.warn("User '${crrUser.userName}' doesn't have permissions to assign request with '$id' id: $requestBody")
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }

    @PutMapping("/requests/{id}/status")
    @JsonView(RequestView.All::class)
    @Throws(RequestException::class)
    fun changeStateRequest(@PathVariable id: Long, @RequestParam status: Boolean,
                           @AuthenticationPrincipal crrUser: User): ResponseEntity<Request>{
        LOGGER.info("User '${crrUser.userName}' tries change status to '$status' for '$id' request")
        return if(crrUser.hasPriorityMoreThan(Role.ROLE_TECHNICIAN)){
            val responseBody = requestService.changeStatus(id, status)
            LOGGER.debug("Change status success, return: $responseBody")
            ResponseEntity(responseBody, HttpStatus.OK)
        } else{
            LOGGER.warn("User '${crrUser.userName}' doesn't have permissions to change status for request with '$id' id")
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }

    @DeleteMapping("/requests/{id}")
    @JsonView(RequestView.CommonView::class)
    @Throws(RequestException::class)
    fun removeRequest(@PathVariable id: Long,
                      @AuthenticationPrincipal crrUser: User): ResponseEntity<String>{
        LOGGER.info("User '${crrUser.userName}' tries to delete request with '$id' id.")
        return if (crrUser.hasPriorityMoreThan(Role.ROLE_TECHNICIAN)){
            requestService.removeRequest(id = id)
            LOGGER.debug("Request with '$id' id was deleted by '${crrUser.userName}' user")
            ResponseEntity(HttpStatus.OK)
        } else {
            LOGGER.warn("User '${crrUser.userName}' doesn't have permissions to delete request with '$id' id")
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }
}