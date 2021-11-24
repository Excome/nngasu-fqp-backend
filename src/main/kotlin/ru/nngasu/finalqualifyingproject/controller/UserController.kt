package ru.nngasu.finalqualifyingproject.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.nngasu.finalqualifyingproject.exception.UserException
import ru.nngasu.finalqualifyingproject.model.User
import ru.nngasu.finalqualifyingproject.service.UserService

/**
@author Peshekhonov Maksim
 */
@RestController
class UserController(private val userService: UserService) {

    @GetMapping("users/{userName}")
    @Throws(UserException::class)
    fun userProfile(@PathVariable userName: String): ResponseEntity<User> {
        val user = userService.getUserByUserName(userName)

        return ResponseEntity(user, HttpStatus.OK)
    }
}