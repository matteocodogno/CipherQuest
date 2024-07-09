package code.nebula.cipherquest.service

import code.nebula.cipherquest.repository.UserLevelRepository
import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.jvm.optionals.getOrDefault

@Service
class GameService(
    private val userLevelRepository: UserLevelRepository,
    private val vectorStore: VectorStore,
) {
    companion object {
        private const val LEVEL_UP_THRESHOLD = 0.82
        private const val DEFAULT_LEVEL = 1
    }

    fun getLevelByUser(userId: String): Int =
        userLevelRepository
            .findById(userId)
            .or {
                Optional.of(userLevelRepository.save(UserLevel(userId, DEFAULT_LEVEL)))
            }
            .map(UserLevel::level)
            .getOrDefault(DEFAULT_LEVEL)

    fun levelUp(id: String, query: String) {
        val level = getLevelByUser(id)

        val matchedQuestionLevel =
            vectorStore
                .similaritySearch(
                    SearchRequest
                        .defaults()
                        .withSimilarityThreshold(LEVEL_UP_THRESHOLD)
                        .withQuery(query)
                        .withFilterExpression("type == 'question'"),
                )
                .minByOrNull { document -> document.metadata["distance"].toString().toFloat() }
                ?.metadata
                ?.get("level")
                ?.toString()
                ?.toInt()
                ?: 0

        if (matchedQuestionLevel == level + 1) {
            userLevelRepository.save(UserLevel(id, matchedQuestionLevel))
        }
    }
}
