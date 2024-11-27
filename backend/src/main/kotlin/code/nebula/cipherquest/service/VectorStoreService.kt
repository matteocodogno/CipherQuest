package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.dto.Message
import code.nebula.cipherquest.repository.VectorStoreRepository
import org.springframework.ai.chat.messages.MessageType
import org.springframework.ai.document.Document
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class VectorStoreService(
    val vectorStore: VectorStore,
    val vectorStoreRepository: VectorStoreRepository,
    @Value("classpath:/prompts/intro-message.st")
    private var introMessageResource: Resource,
) {
    fun loadDocument(docs: List<Document>) = vectorStore.add(TokenTextSplitter().split(docs))

    fun search(query: String): List<Document> = vectorStore.similaritySearch(query)

    fun existsDocumentWithFileName(filename: String) = vectorStoreRepository.existsDocumentWithFileName(filename)

    fun getDocumentByFilename(
        filename: String,
        level: Int,
    ) = vectorStoreRepository
        .getDocumentByFilename(filename, level)

    fun getMessageHistoryByUserId(userId: String): List<Message> =
        vectorStoreRepository.getMessageHistoryByUserId(userId).ifEmpty {
            val initialMessage =
                Message.getInitialMessage(introMessageResource.getContentAsString(Charsets.UTF_8), userId)

            listOf(initialMessage).apply {
                vectorStore.add(
                    map { (_, message) ->
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
}
