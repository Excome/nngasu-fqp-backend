package ru.nngasu.finalqualifyingproject.controller

import jdk.jshell.spi.ExecutionControl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.nngasu.finalqualifyingproject.exception.UserException
import ru.nngasu.finalqualifyingproject.model.User
import ru.nngasu.finalqualifyingproject.service.UserService
import kotlin.jvm.Throws

/**
@author Peshekhonov Maksim
 */
@RestController
class UserController(private val userService: UserService) {

    @GetMapping("users/{userName}")
    open fun userProfile(@PathVariable userName: String): ResponseEntity<User> {
        val user = userService.getUserByUserName(userName)

        return ResponseEntity(user, HttpStatus.OK)
    }

    @PostMapping("/signup")
    @Throws(UserException::class)
    open fun registration(@RequestBody body: Map<String, String>): ResponseEntity<User> {
        val user = userService.createUser(
            userName = body["username"]!!,
            email = body["email"]!!,
            pass = body["password"]!!,
            passConfirm = body["passwordConf"]!!
        )
        return ResponseEntity(user, HttpStatus.OK)
    }
}