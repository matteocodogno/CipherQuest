package code.nebula.cipherquest.service

import code.nebula.cipherquest.configuration.properties.UniqueCodeMailProperties
import code.nebula.cipherquest.repository.gcs.EmailTemplateRepository
import jakarta.mail.internet.MimeMessage
import jakarta.mail.util.ByteArrayDataSource
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
        var message =
            emailTemplateRepository.findUniqueCodeByStoryName(storyName).let {
                ST(String(it.getContent()), '+', '+')
                    .apply {
                        add("uniqueCode", uniqueCode)
                        add("username", username)
                    }.render()
            }

        val assets =
            emailTemplateRepository
                .getAssetsByStoryName(storyName)
                .filter { it.size > 0 }

        assets.forEach {
            val filename = it.name.substringAfterLast("assets/")
            message = message.replace(filename, "cid:${it.generation}")
        }

        val mimeMessage: MimeMessage = emailSender.createMimeMessage()

        MimeMessageHelper(mimeMessage, true).apply {
            setFrom(uniqueCodeMailProperties.from)
            setTo(email)
            setSubject(uniqueCodeMailProperties.subject)
            setText(message, true)

            assets.forEach {
                val dataSource = ByteArrayDataSource(it.getContent(), it.contentType)
                addInline("${it.generation}", dataSource)
            }
        }

        emailSender.send(mimeMessage)
    }
}
