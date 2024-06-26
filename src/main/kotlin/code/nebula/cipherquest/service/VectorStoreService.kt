package code.nebula.cipherquest.service

import org.springframework.ai.document.Document
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service

@Service
class VectorStoreService(
    val vectorStore: VectorStore,
) {
    fun loadDocument(docs: List<Document>) = vectorStore.add(TokenTextSplitter().split(docs))

    fun search(query: String): List<Document> = vectorStore.similaritySearch(query)
}
