package ru.nngasu.finalqualifyingproject.controller

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

    @PostMapping("/requests")
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


}