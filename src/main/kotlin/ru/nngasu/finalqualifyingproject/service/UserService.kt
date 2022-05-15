package ru.nngasu.finalqualifyingproject.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
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
    @Autowired
    private lateinit var mailService: MailService

    fun createUser(user: User): User {
        var userFromDb = userRepository.findUserByEmail(email = user.email)
        if (userFromDb != null)
            throw UserException("User with email '${user.email}' already exist", UserError.EMAIL_IS_ALREADY_USED)

        userFromDb = userRepository.findUserByUserName(userName = user.userName)
        if (userFromDb != null)
            throw UserException("User '${user.userName}' already exist", UserError.USERNAME_IS_ALREADY_USED)

        if (user.pass != user.passConfirm)
            throw UserException("Entered passwords aren't equal", UserError.PASSWORDS_ARE_NOT_EQUAL)

        user.pass = this.passwordEncoder.encode(user.pass)
        user.verificationCode = user.hashCode().toString()
//        mailService.sendVerificationEmail(user)

        return userRepository.save(user)
    }

    fun getUserByUserName(userName: String): User {
        return userRepository.findUserByUserName(userName)
            ?: throw UserException("User '${userName}' not found!", UserError.USER_NOT_FOUND)
    }

    fun getUserByEmail(email: String): User {
        return userRepository.findUserByEmail(email)
            ?: throw UserException("User with email '${email} not found!'", UserError.USER_NOT_FOUND)
    }

    fun changeUserProfile(user: User): User {
        var userFromDb = getUserByUserName(user.userName)

        userFromDb.firstName = user.firstName
        userFromDb.surName = user.surName
        userFromDb.roles = user.roles

        return userRepository.save(userFromDb)
    }

    fun changeUserEmail(user: User): User {
        var userFromDb: User = getUserByUserName(user.userName)

        if (userRepository.findUserByEmail(user.email) != null)
            throw UserException("Unable change '${userFromDb.email}' email for '${userFromDb.userName} user. Email '${user.email}' already used'", UserError.EMAIL_IS_ALREADY_USED)

        userFromDb.email = user.email

        return userRepository.save(user)
    }

    fun changeUserUsername(user: User, newUserName: String): User {
        var userFromDb: User = getUserByUserName(user.userName)

        if (userRepository.findUserByUserName(newUserName) != null)
            throw UserException("Unable change '${user.userName}' username. Username '${newUserName}' already used'", UserError.USERNAME_IS_ALREADY_USED)

        userFromDb.userName = newUserName

        return userRepository.save(user)
    }

    fun changeUserPassword(user: User): User{
        var userFromDb: User = getUserByUserName(user.userName)

        if (user.pass != user.passConfirm)
            throw UserException("Unable change password for '${user.userName}' username, pass and passConf aren't equal", UserError.PASSWORDS_ARE_NOT_EQUAL)

        userFromDb.pass = this.passwordEncoder.encode(user.pass)
        return userRepository.save(userFromDb)
    }

    fun getUsers(pageable: Pageable): MutableList<User> {
        val userList = userRepository.findAll(pageable).content

        if(userList.isEmpty())
            throw UserException("Unable find users at ${pageable.pageNumber} page.", UserError.USER_NOT_FOUND)
        return userList
    }

    fun getUsersByUsername(userName: String?, pageable: Pageable): MutableList<User> {
        val userList = userRepository.findAllByUserNameContains(userName, pageable).content

        if(userList.isEmpty())
            throw UserException("Unable find users contains '${userName}' in userName at ${pageable.pageNumber} page.", UserError.USER_NOT_FOUND)
        return userList
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        return getUserByUserName(username!!)
    }

    fun verifyUser(userName: String, code: String): User {
        val user = userRepository.findUserByUserNameAndVerificationCode(userName, code)
            ?: throw UserException("User '$userName' with '$code' verification code not found", UserError.USER_NOT_FOUND)

        user.enabled = true
        return userRepository.save(user)
    }
}