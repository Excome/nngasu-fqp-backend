package ru.nngasu.finalqualifyingproject.service

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import ru.nngasu.finalqualifyingproject.model.User

/**
@author Peshekhonov Maksim
 */
@Service
class MailService(
    @Autowired
    private val mailSeder: JavaMailSender
) {
    private val LOGGER: Logger = LogManager.getLogger(MailService::class)

    @Value("spring.mail.username")
    private lateinit var SERVICE_EMAIL: String
    private val SERVICE_NAME = "NNGASU"
    private val EMAIL_RESOURCE_PATH = "/templates/email/"
    private val VERIFICATION_TEMPLATE = "verification.html"

    fun sendVerificationEmail(user: User) {
        val userEmail = user.email
        val fullName = "${user.surName} '${user.userName}' ${user.firstName}"
        val url = "${getBaseUrl()}/verify?userName=${user.userName}&code=${user.verificationCode}"
        var content = getContentFromResourceFile(VERIFICATION_TEMPLATE)
            .replace("[[name]]", fullName)
        content = content.replace("[[url]]", url)
        val subject = "Верификация личного кабинета преподавателя"
        LOGGER.info("Sending verification email to '${user.userName}'")
        sendEmail(to = userEmail,
            from = SERVICE_EMAIL,
            subject = subject,
            content = content)
    }

    fun sendEmail(to: String, from: String, subject: String, content: String){
        val message = mailSeder.createMimeMessage()
        val helper = MimeMessageHelper(message)

        helper.setFrom(from, SERVICE_NAME)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(content, true)

        mailSeder.send(message)
    }

    fun getContentFromResourceFile(fileName: String): String {
        val path = EMAIL_RESOURCE_PATH + fileName
        var emailTemplate = ""
        try {
            emailTemplate = javaClass.getResource(path).readText()
            return emailTemplate
        } catch (e: Exception) {
            LOGGER.error("Email template with '$fileName' name not found: ", e)
        }
        return emailTemplate
    }

    fun getBaseUrl(): String{
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
    }
}