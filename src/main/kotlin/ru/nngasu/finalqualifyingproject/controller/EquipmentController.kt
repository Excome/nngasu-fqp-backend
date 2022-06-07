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
import ru.nngasu.finalqualifyingproject.exception.EquipmentException
import ru.nngasu.finalqualifyingproject.model.Equipment
import ru.nngasu.finalqualifyingproject.model.Role
import ru.nngasu.finalqualifyingproject.model.User
import ru.nngasu.finalqualifyingproject.model.jsonView.EquipmentView
import ru.nngasu.finalqualifyingproject.service.EquipmentService

/**
 * @author Peshekhonov Maksim
 */
@RestController
class EquipmentController {
    private val LOGGER: Logger = LogManager.getLogger(EquipmentController::class)

    @Autowired
    private lateinit var equipmentService: EquipmentService

    @GetMapping("/equipments")
    @JsonView(EquipmentView.All::class)
    @Throws(EquipmentException::class)
    fun getEquipments(@RequestParam(required = false) name: String?,
                      @RequestParam(required = false) type: String?,
                      @PageableDefault(size = 10, sort = ["id"],
                          direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<List<Equipment>>{
        val responseBody =
            if (!name.isNullOrBlank() && type.isNullOrBlank()) {
                LOGGER.info("Gonna get a list of equipments by '$name' name at '${pageable.pageNumber}' page")
                equipmentService.getEquipmentsByName(name, pageable)
            }
            else if (!type.isNullOrBlank() && name.isNullOrBlank()) {
                LOGGER.info("Gonna get a list of equipments by '$type' type at '${pageable.pageNumber}' page")
                equipmentService.getEquipmentsByType(type, pageable)
            }
            else {
                LOGGER.info("Gonna get a list of equipments at '${pageable.pageNumber}' page")
                equipmentService.getEquipments(pageable)
            }

        LOGGER.debug("Return list of equipments: $responseBody")
        return ResponseEntity(responseBody, HttpStatus.OK)
    }

    @GetMapping("/equipments/{name}")
    @JsonView(EquipmentView.All::class)
    @Throws(EquipmentException::class)
    fun getEquipment(@PathVariable name: String): ResponseEntity<Equipment>{
        LOGGER.info("Gonna get equipment with '$name' name")
        val responseBody = equipmentService.getEquipmentByName(name)

        LOGGER.debug("Return equipment: $responseBody")
        return ResponseEntity(responseBody, HttpStatus.OK)
    }

    @PostMapping("/equipments")
    @JsonView(EquipmentView.Common::class)
    @Throws(EquipmentException::class)
    fun createEquipment(@RequestBody equipment: Equipment): ResponseEntity<Equipment>{
        LOGGER.info("Request for create equipment: $equipment")
        val responseBody = equipmentService.createEquipment(equipment)

        LOGGER.debug("Equipment $equipment was created.")
        return ResponseEntity(responseBody, HttpStatus.OK)
    }

    @PutMapping("/equipments/{name}")
    @JsonView(EquipmentView.Common::class)
    @Throws(EquipmentException::class)
    fun editEquipment(@PathVariable name: String, @RequestBody equipment: Equipment,
                      @AuthenticationPrincipal user: User): ResponseEntity<Equipment>{

        LOGGER.info("User '${user.userName}' gonna edit equipment with '$name' name")
        return if (user.roles.contains(Role.ROLE_MODERATOR) || user.roles.contains(Role.ROLE_ADMIN)) {
            val responseBody = equipmentService.changeEquipment(name, equipment)
            ResponseEntity(responseBody, HttpStatus.OK)
        } else {
            LOGGER.error("User '${user.userName} with '${user.roles}' roles try to edit equipment with '$name' name. Access forbidden.")
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }

    @DeleteMapping("/equipments/{name}")
    @JsonView(EquipmentView.Common::class)
    @Throws(EquipmentException::class)
    fun removeEquipment(@PathVariable name: String,
                        @AuthenticationPrincipal user: User): ResponseEntity<String>{
        LOGGER.info("User '${user.userName}' gonna delete equipment with '$name' name")
        return if (user.roles.contains(Role.ROLE_ADMIN) || user.roles.contains(Role.ROLE_MODERATOR)){
            equipmentService.removeEquipment(name)
            ResponseEntity(HttpStatus.OK)
        } else {
            LOGGER.error("User '${user.userName} with '${user.roles}' roles try to remove equipment with '$name' name. Access forbidden.")
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }
}