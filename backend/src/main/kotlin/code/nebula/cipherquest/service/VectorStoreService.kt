package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.dto.Message
import code.nebula.cipherquest.models.dto.Source
import code.nebula.cipherquest.repository.VectorStoreRepository
import org.json.JSONObject
import org.springframework.ai.chat.messages.MessageType
import org.springframework.ai.document.Document
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
@Suppress("TooManyFunctions")
class VectorStoreService(
    val vectorStore: VectorStore,
    val vectorStoreRepository: VectorStoreRepository,
    @Value("classpath:/prompts/intro-message.st")
    private var introMessageResource: Resource,
) {
    fun loadDocument(docs: List<Document>) = vectorStore.add(TokenTextSplitter().split(docs))

    fun countUserMessages(id: String): Int = vectorStoreRepository.countUserMessages(id)

    fun existsDocumentWithSource(filename: String) = vectorStoreRepository.existsDocumentWithSource(filename)

    fun getDocumentById(id: String): String? =
        vectorStoreRepository
            .getDocumentById(id)

    fun getDocumentByFilename(
        filename: String,
        level: Int,
    ): String? =
        vectorStoreRepository
            .getDocumentByFilename(filename, level)

    fun getAllDiaryPages(level: Int): List<Source>? =
        vectorStoreRepository
            .getAllDiaryPages(level)

    fun updateInfo(
        id: String,
        info: Map<String, Any>?,
    ) {
        vectorStoreRepository.updateInfo(id, JSONObject(info).toString())
    }

    fun updateInfoOnLastMessage(
        userId: String,
        info: Map<String, Any>?,
    ) {
        updateInfo(getLastMessage(userId).id, info)
    }

    fun getLastMessage(userId: String): Message = vectorStoreRepository.getMessageHistoryByUserId(userId).last()

    fun getMessageHistoryByUserId(userId: String): List<Message> =
        vectorStoreRepository.getMessageHistoryByUserId(userId).ifEmpty {
            val initialMessage =
                Message.getInitialMessage(introMessageResource.getContentAsString(Charsets.UTF_8), userId)

            listOf(initialMessage).apply {
                vectorStore.add(
                    map { (_, _, message) ->
                        Document(
                            message,
                            mapOf(
                                "conversationId" to userId,
                                "messageType" to MessageType.ASSISTANT.name,
                            ),
                        )
                    },
                )
            }
        }

    fun saveMessage(
        userId: String,
        message: String,
        messageType: MessageType,
    ) {
        vectorStore.add(
            listOf(
                Document(
                    message,
                    mapOf(
                        "conversationId" to userId,
                        "messageType" to messageType.name,
                    ),
                ),
            ),
        )
    }
}
