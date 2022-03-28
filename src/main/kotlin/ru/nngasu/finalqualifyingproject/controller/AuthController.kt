package ru.nngasu.finalqualifyingproject.controller

import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.nngasu.finalqualifyingproject.config.jwt.JwtProvider
import ru.nngasu.finalqualifyingproject.exception.UserException
import ru.nngasu.finalqualifyingproject.model.User
import ru.nngasu.finalqualifyingproject.model.jsonView.UserView
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
    @JsonView(UserView.Profile::class)
    @Throws(UserException::class)
    fun registration(@RequestBody user: User): ResponseEntity<User> {
        val responseBody = userService.createUser(user)
        return ResponseEntity(responseBody, HttpStatus.OK)
    }

    @GetMapping("/verify")
    @JsonView(UserView.Profile::class)
    @Throws(UserException::class)
    fun verifyEmail(@RequestParam userName: String, @RequestParam code: String): ResponseEntity<User> {
        val responseBody = userService.verifyUser(userName, code)
        return ResponseEntity(responseBody, HttpStatus.OK)
    }
}