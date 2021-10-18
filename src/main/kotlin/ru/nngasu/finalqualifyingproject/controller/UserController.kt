package ru.nngasu.finalqualifyingproject.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.nngasu.finalqualifyingproject.model.User
import ru.nngasu.finalqualifyingproject.service.UserService

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("users/{userName}")
    open fun userProfile(@PathVariable userName: String): ResponseEntity<User> {
        val user = userService.getUserByUserName(userName)

        return ResponseEntity(user, HttpStatus.OK)
    }
}