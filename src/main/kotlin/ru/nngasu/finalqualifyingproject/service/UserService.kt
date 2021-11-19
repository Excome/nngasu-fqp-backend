package ru.nngasu.finalqualifyingproject.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.nngasu.finalqualifyingproject.exception.UserException
import ru.nngasu.finalqualifyingproject.exception.error.UserError
import ru.nngasu.finalqualifyingproject.model.User
import ru.nngasu.finalqualifyingproject.repository.UserRepository

/**
@author Peshekhonov Maksim
 */
@Service
class UserService : UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    fun createUser(userName: String, email: String, pass: String, passConfirm: String): User{
        var userFromDb = userRepository.findUserByEmail(email = email)
        if (userFromDb != null)
            throw UserException("User with email '${email}' already exist", UserError.EMAIL_IS_ALREADY_USED)

        userFromDb = userRepository.findUserByUserName(userName = userName)
        if (userFromDb != null)
            throw UserException("User '${userName}' already exist", UserError.USERNAME_IS_ALREADY_USED)

        if (pass != passConfirm)
            throw UserException("Entered passwords aren't equal", UserError.PASSWORDS_ARE_NOT_EQUAL)

        val passEncode = this.passwordEncoder.encode(pass)
        return userRepository.save(User(userName = userName, pass = passEncode, email = email))
    }

    fun getUserByUserName(userName: String): User {
        return userRepository.findUserByUserName(userName)
            ?: throw UserException("User '${userName}' not found!", UserError.USER_NOT_FOUND)
    }

    fun getUserByEmail(email: String): User {
        return userRepository.findUserByEmail(email)
            ?: throw UserException("User with email '${email} not found!'", UserError.USER_NOT_FOUND)
    }

    fun saveUser(user: User): User {
        return userRepository.save(user)
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        return getUserByUserName(username!!)
    }
}