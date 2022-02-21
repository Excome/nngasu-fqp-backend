package ru.nngasu.finalqualifyingproject.controller

import com.fasterxml.jackson.annotation.JsonView
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.nngasu.finalqualifyingproject.exception.UserException
import ru.nngasu.finalqualifyingproject.model.Role
import ru.nngasu.finalqualifyingproject.model.User
import ru.nngasu.finalqualifyingproject.model.jsonView.UserView
import ru.nngasu.finalqualifyingproject.service.UserService

/**
@author Peshekhonov Maksim
 */
@RestController
class UserController(private val userService: UserService) {
    private val LOGGER: Logger = LogManager.getLogger(UserController::class)

    @GetMapping("/users")
    @JsonView(UserView.Common::class)
    @Throws(UserException::class)
    fun getUsersList(@RequestParam(required = false) userName: String?,
                     @PageableDefault(size = 10, sort = ["createdDate"],
                         direction = Sort.Direction.DESC) pageable: Pageable): ResponseEntity<MutableList<User>> {

        val users = if (userName.isNullOrBlank()){
            LOGGER.info("Gonna get a list of users at '${pageable.pageNumber}' page")
            userService.getUsers(pageable)
        } else {
            LOGGER.info("Gonna get a list of users by '${userName}' at '${pageable.pageNumber}' page")
            userService.getUsersByUsername(userName, pageable)
        }

        LOGGER.debug("Return list of users: ${users}")
        return ResponseEntity(users, HttpStatus.OK)
    }

    @GetMapping("/users/{userName}")
    @JsonView(UserView.Profile::class)
    @Throws(UserException::class)
    fun userProfile(@PathVariable userName: String): ResponseEntity<User> {
        LOGGER.info("Gonna get user by '${userName} username'")
        val user = userService.getUserByUserName(userName)

        LOGGER.debug("Return '${user}'")
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PutMapping("/users/{userName}")
    @JsonView(UserView.Profile::class)
    @Throws(UserException::class)
    fun editUserProfile(@PathVariable userName: String,  @RequestBody user: User,
                        @AuthenticationPrincipal currentUser: User): ResponseEntity<User>{
        return if (currentUser.userName == userName || currentUser.roles.contains(Role.ROLE_MODERATOR)
            || currentUser.roles.contains(Role.ROLE_ADMIN)){
            user.userName = userName
            val responseBody = userService.changeUserProfile(user)
            ResponseEntity<User>(responseBody, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }

    @PutMapping("/users/{userName}/change-pass")
    @Throws(UserException::class)
    fun changeUserPassword(@PathVariable userName: String, @RequestBody user: User,
                           @AuthenticationPrincipal currentUser: User): ResponseEntity<User>{
        return if (currentUser.userName == userName || currentUser.roles.contains(Role.ROLE_MODERATOR)
            || currentUser.roles.contains(Role.ROLE_ADMIN)){
            user.userName = userName
            userService.changeUserPassword(user)
            ResponseEntity<User>(HttpStatus.OK)
        }else {
            ResponseEntity(HttpStatus.FORBIDDEN)
        }

    }

    @PutMapping("/users/{userName}/change-email")
    @JsonView(UserView.Profile::class)
    @Throws(UserException::class)
    fun changeUserEmail(@PathVariable userName: String, @RequestBody user: User,
                        @AuthenticationPrincipal currentUser: User): ResponseEntity<User>{
        return if (currentUser.userName == userName || currentUser.roles.contains(Role.ROLE_MODERATOR)
            || currentUser.roles.contains(Role.ROLE_ADMIN)){
            user.userName = userName
            userService.changeUserEmail(user)
            ResponseEntity<User>(HttpStatus.OK)
        }else {
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }
}