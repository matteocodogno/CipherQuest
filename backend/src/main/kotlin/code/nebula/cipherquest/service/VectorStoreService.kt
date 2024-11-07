package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.dto.Message
import code.nebula.cipherquest.repository.VectorStoreRepository
import org.springframework.ai.chat.messages.MessageType
import org.springframework.ai.chat.prompt.PromptTemplate
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

    fun getMessageHistoryByUserId(userId: String): List<Message> =
        vectorStoreRepository.getMessageHistoryByUserId(userId).ifEmpty {
            val introMessage = introMessageResource.getContentAsString(Charsets.UTF_8)
            val introMessageInterpolated =
                PromptTemplate(introMessage)
                    .create(mapOf("userId" to userId))
                    .contents

            vectorStore.add(
                listOf(
                    Document(
                        introMessageInterpolated,
                        mapOf(
                            "conversationId" to userId,
                            "messageType" to MessageType.ASSISTANT.name,
                        ) as Map<String, Any>,
                    ),
                ),
            )

            vectorStoreRepository.getMessageHistoryByUserId(userId)
        }
}
