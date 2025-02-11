package code.nebula.cipherquest.service

import code.nebula.cipherquest.configuration.properties.UniqueCodeMailProperties
import code.nebula.cipherquest.repository.gcs.EmailTemplateRepository
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
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
                ST(String(it.getContent()), '{', '}')
                    .apply {
                        add("uniqueCode", uniqueCode)
                        add("username", username)
                    }.render()
            }

        SimpleMailMessage()
            .apply {
                from = uniqueCodeMailProperties.from
                setTo(email)
                subject = uniqueCodeMailProperties.subject
                text = message
            }.also { emailSender.send(it) }
    }
}
