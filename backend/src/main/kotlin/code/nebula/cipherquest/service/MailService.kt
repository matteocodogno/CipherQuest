package code.nebula.cipherquest.service

import code.nebula.cipherquest.configuration.properties.UniqueCodeMailProperties
import code.nebula.cipherquest.exceptions.EmailSendingException
import code.nebula.cipherquest.exceptions.EmailTemplateNotFoundException
import code.nebula.cipherquest.repository.gcs.EmailTemplateRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.mail.MessagingException
import jakarta.mail.util.ByteArrayDataSource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.stringtemplate.v4.ST

private val logger = KotlinLogging.logger {}

/**
 * Service responsible for sending emails, particularly those containing unique codes for specific stories.
 *
 * This service interacts with an email sender, email templates, and associated assets
 * to generate and send rich emails while handling potential failures gracefully.
 *
 * @property emailSender Handles the email sending process
 * @property emailTemplateRepository Repository for fetching email templates and assets
 * @property uniqueCodeMailProperties Configuration for unique code email properties such as sender and subject
 */
@Service
class MailService(
    private val emailSender: JavaMailSender,
    private val emailTemplateRepository: EmailTemplateRepository,
    private val uniqueCodeMailProperties: UniqueCodeMailProperties,
) {
    /**
     * Sends an email containing a unique code to a user for a specific story.
     * The email is based on a template and includes story-specific assets.
     *
     * @param email The recipient's email address
     * @param storyName The name of the story for which the code is being sent
     * @param username The recipient's username
     * @param uniqueCode The unique code to be included in the email
     * @throws EmailTemplateNotFoundException if the email template cannot be found
     * @throws EmailSendingException if there's an error during email sending
     */
    fun sendUniqueCodeEmail(
        email: String,
        storyName: String,
        username: String,
        uniqueCode: String,
    ) = runCatching {
        val emailContent = prepareEmailContent(storyName, username, uniqueCode)
        val assets = loadAssets(storyName)
        val finalContent = processAssetsInContent(emailContent, assets)

        createAndSendEmail(email, finalContent, assets)
        logger.info { "Successfully sent unique code email to $email for story: $storyName" }
    }.onFailure { e ->
        logger.error(e) { "Failed to send email to $email for story: $storyName" }
        throw when (e) {
            is MessagingException -> EmailSendingException("Failed to create or send email", e)
            else -> EmailTemplateNotFoundException("Failed to process email template", e)
        }
    }

    /**
     * Prepares the email content by loading the template and replacing placeholders.
     */
    private fun prepareEmailContent(
        storyName: String,
        username: String,
        uniqueCode: String,
    ): String =
        emailTemplateRepository
            .findUniqueCodeByStoryName(storyName)
            .getContent()
            .let { String(it) }
            .let { content ->
                ST(content, '+', '+')
                    .apply {
                        add("uniqueCode", uniqueCode)
                        add("username", username)
                    }.render()
            }

    /**
     * Loads and transforms story assets into a structured format.
     */
    private fun loadAssets(storyName: String): List<Asset> =
        emailTemplateRepository
            .getAssetsByStoryName(storyName)
            .filter { it.size > 0 }
            .map { blob ->
                Asset(
                    filename = blob.name.substringAfterLast("assets/"),
                    generation = blob.generation,
                    content = blob.getContent(),
                    contentType = blob.contentType,
                )
            }

    /**
     * Processes assets references in the content, replacing filenames with CID references.
     */
    private fun processAssetsInContent(
        content: String,
        assets: List<Asset>,
    ): String =
        assets.fold(content) { acc, asset ->
            acc.replace(asset.filename, "cid:${asset.generation}")
        }

    /**
     * Creates and sends the email with the given content and assets.
     */
    private fun createAndSendEmail(
        email: String,
        content: String,
        assets: List<Asset>,
    ) {
        emailSender
            .createMimeMessage()
            .also { mime ->
                MimeMessageHelper(mime, true).apply {
                    setFrom(uniqueCodeMailProperties.from)
                    setTo(email)
                    setSubject(uniqueCodeMailProperties.subject)
                    setText(content, true)
                    assets.forEach { asset ->
                        addInline(
                            "${asset.generation}",
                            ByteArrayDataSource(asset.content, asset.contentType),
                        )
                    }
                }
            }.let(emailSender::send)
    }
}

/**
 * Represents an immutable asset typically used for attaching resources, such as images or files,
 * to other objects or services. Each asset is defined by a combination of a filename,
 * a generation identifier, its binary content, and a content type indicating the type of the file.
 *
 * The `generation` field can be used as a unique identifier for a particular version of the asset,
 * while the `contentType` helps correctly handling or rendering the asset.
 *
 * Equality and hash code are overridden to perform deep comparisons, including checking the byte content equality.
 *
 * @property filename The name of the file represented as a string.
 * @property generation The version or generation of the resource, often used for identifying different revisions.
 * @property content The raw binary content of the asset being represented.
 * @property contentType The MIME type of the content, e.g., "image/png", "application/pdf".
 */
private data class Asset(
    val filename: String,
    val generation: Long,
    val content: ByteArray,
    val contentType: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Asset

        if (filename != other.filename) return false
        if (generation != other.generation) return false
        if (!content.contentEquals(other.content)) return false
        if (contentType != other.contentType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = filename.hashCode()
        result = 31 * result + generation.hashCode()
        result = 31 * result + content.contentHashCode()
        result = 31 * result + contentType.hashCode()
        return result
    }
}
