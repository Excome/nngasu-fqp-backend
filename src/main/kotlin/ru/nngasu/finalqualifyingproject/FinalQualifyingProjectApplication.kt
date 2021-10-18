package ru.nngasu.finalqualifyingproject

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.nngasu.finalqualifyingproject.model.Role
import ru.nngasu.finalqualifyingproject.service.UserService

@SpringBootApplication
class FinalQualifyingProjectApplication


fun main(args: Array<String>) {
	val context = runApplication<FinalQualifyingProjectApplication>(*args)
	val userService: UserService = context.getBean(UserService::class)
//	userService.createUser("excome", "test1111", "test@gmail.com")
//	var user = userService.getUserByUserName("test")
//	print(user.roles)
//	user.roles.add(Role.ADMINISTRATOR)
//	user = userService.saveUser(user)
//	print(user.roles)
}
