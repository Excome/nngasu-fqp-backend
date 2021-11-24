package ru.nngasu.finalqualifyingproject.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.nngasu.finalqualifyingproject.config.jwt.JwtProvider
import ru.nngasu.finalqualifyingproject.exception.UserException
import ru.nngasu.finalqualifyingproject.model.User
import ru.nngasu.finalqualifyingproject.service.UserService

/**
@author Peshekhonov Maksim
 */
@RestController
class AuthController(
    private val userService: UserService,
    private val jwtProvider: JwtProvider
) {
    @PostMapping("/registration")
    @Throws(UserException::class)
    fun registration(@RequestBody body: Map<String, String>): ResponseEntity<User> {
        val user = userService.createUser(
            userName = body["username"]!!,
            email = body["email"]!!,
            pass = body["password"]!!,
            passConfirm = body["passwordConf"]!!
        )
        return ResponseEntity(user, HttpStatus.OK)
    }
}