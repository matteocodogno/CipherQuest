/*
 * Copyright 2024-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package code.nebula.cipherquest.advisor

import code.nebula.cipherquest.DocumentType
import org.springframework.ai.chat.client.AdvisedRequest
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.MessageType
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.model.Generation
import org.springframework.ai.chat.model.MessageAggregator
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import reactor.core.publisher.Flux
import java.util.stream.Collectors

/**
 * Memory is retrieved from a VectorStore added into the prompt's system text.
 *
 * @author Christian Tzolov
 * @since 1.0.0 M1
 */
class PgVectorStoreChatMemoryAdvisor : AbstractChatMemoryAdvisor<VectorStore?> {
    private val systemTextAdvise: String

    @JvmOverloads
    constructor(vectorStore: VectorStore?, systemTextAdvise: String = DEFAULT_SYSTEM_TEXT_ADVISE) : super(vectorStore) {
        this.systemTextAdvise = systemTextAdvise
    }

    @JvmOverloads
    constructor(
        vectorStore: VectorStore?, defaultConversationId: String?,
        chatHistoryWindowSize: Int, systemTextAdvise: String = DEFAULT_SYSTEM_TEXT_ADVISE
    ) : super(vectorStore, defaultConversationId, chatHistoryWindowSize) {
        this.systemTextAdvise = systemTextAdvise
    }

    override fun adviseRequest(request: AdvisedRequest, context: Map<String, Any>): AdvisedRequest {
        val advisedSystemText = request.systemText() + System.lineSeparator() + this.systemTextAdvise

        val searchRequest = SearchRequest.query(request.userText())
            .withTopK(this.doGetChatMemoryRetrieveSize(context))
            .withFilterExpression(
                DOCUMENT_METADATA_CONVERSATION_ID + "=='" + this.doGetConversationId(context) + "' && type == '${DocumentType.MEMORY}'"
            )

        val documents =
            getChatMemoryStore()!!.similaritySearch(searchRequest)

        val longTermMemory = documents.stream()
            .map { obj: Document -> obj.content }
            .collect(Collectors.joining(System.lineSeparator()))

        val advisedSystemParams: MutableMap<String, Any> = HashMap(request.systemParams())
        advisedSystemParams["long_term_memory"] = longTermMemory

        val advisedRequest = AdvisedRequest.from(request)
            .withSystemText(advisedSystemText)
            .withSystemParams(advisedSystemParams)
            .build()

        val userMessage = UserMessage(request.userText(), request.media())
        getChatMemoryStore()!!.write(
            toDocuments(
                java.util.List.of<Message>(userMessage),
                this.doGetConversationId(context)
            )
        )

        return advisedRequest
    }

    override fun adviseResponse(chatResponse: ChatResponse, context: Map<String, Any>): ChatResponse {
        val assistantMessages = chatResponse.results.stream().map { g: Generation -> g.output as Message }
            .toList()

        getChatMemoryStore()!!.write(toDocuments(assistantMessages, this.doGetConversationId(context)))

        return chatResponse
    }

    override fun adviseResponse(fluxChatResponse: Flux<ChatResponse>, context: Map<String, Any>): Flux<ChatResponse> {
        return MessageAggregator().aggregate(fluxChatResponse) { chatResponse: ChatResponse ->
            val assistantMessages = chatResponse.results
                .stream()
                .map { g: Generation -> g.output as Message }
                .toList()
            getChatMemoryStore()!!.write(toDocuments(assistantMessages, this.doGetConversationId(context)))
        }
    }

    private fun toDocuments(messages: List<Message>, conversationId: String): List<Document> {
        return messages.stream()
            .filter { m: Message -> m.messageType == MessageType.USER || m.messageType == MessageType.ASSISTANT }
            .map { message: Message ->
                val metadata = HashMap(if (message.metadata != null) message.metadata else HashMap())
                metadata[DOCUMENT_METADATA_CONVERSATION_ID] = conversationId
                metadata[DOCUMENT_METADATA_MESSAGE_TYPE] = message.messageType.name
                metadata["type"] = DocumentType.MEMORY
                val doc = Document(message.content, metadata)
                doc
            }
            .toList()
    }

    companion object {
        private const val DOCUMENT_METADATA_CONVERSATION_ID = "conversationId"

        private const val DOCUMENT_METADATA_MESSAGE_TYPE = "messageType"

        private val DEFAULT_SYSTEM_TEXT_ADVISE = """
			Use the long term conversation memory from the LONG_TERM_MEMORY section to provide accurate answers.
			---------------------
			LONG_TERM_MEMORY:
			{long_term_memory}
			---------------------
			""".trimIndent()
    }
}
