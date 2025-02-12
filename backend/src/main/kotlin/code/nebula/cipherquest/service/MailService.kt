package code.nebula.cipherquest.service

import code.nebula.cipherquest.configuration.properties.UniqueCodeMailProperties
import code.nebula.cipherquest.repository.gcs.EmailTemplateRepository
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.stringtemplate.v4.ST

@Service
class MailService(
    private val emailSender: JavaMailSender,
    private val emailTemplateRepository: EmailTemplateRepository,
    private val uniqueCodeMailProperties: UniqueCodeMailProperties,
) {
    fun sendUniqueCodeEmail(
        email: String,
        storyName: String,
        username: String,
        uniqueCode: String,
    ) {
        val message =
            emailTemplateRepository.findUniqueCodeByStoryName(storyName).let {
                ST(String(it.getContent()), '+', '+')
                    .apply {
                        add("uniqueCode", uniqueCode)
                        add("username", username)
                    }.render()
            }

        val mimeMessage: MimeMessage = emailSender.createMimeMessage()

        MimeMessageHelper(mimeMessage, true).apply {
            setFrom(uniqueCodeMailProperties.from)
            setTo(email)
            setSubject(uniqueCodeMailProperties.subject)
            setText(message, true)
        }

        emailSender.send(mimeMessage)
    }
}
