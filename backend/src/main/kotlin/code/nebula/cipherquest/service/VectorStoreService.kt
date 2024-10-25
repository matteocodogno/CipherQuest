package code.nebula.cipherquest.service

import code.nebula.cipherquest.repository.VectorStoreRepository
import org.springframework.ai.document.Document
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service

@Service
class VectorStoreService(
    val vectorStore: VectorStore,
    val vectorStoreRepository: VectorStoreRepository,
) {
    fun loadDocument(docs: List<Document>) = vectorStore.add(TokenTextSplitter().split(docs))

    fun search(query: String): List<Document> = vectorStore.similaritySearch(query)

    fun existsDocumentWithFileName(filename: String) = vectorStoreRepository.existsDocumentWithFileName(filename)

    fun getMessageHistoryByUserId(userId: String) = vectorStoreRepository.getMessageHistoryByUserId(userId)
}
