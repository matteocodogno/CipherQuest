package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.dto.GameDataFile
import code.nebula.cipherquest.models.requests.FixedBotMessagesRequest
import code.nebula.cipherquest.repository.gcs.GcsStreamRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.lang.Boolean.TRUE

@Service
class GCloudService(
    private val fixedBotMessageService: FixedBotMessageService,
    private val actionableQuestionService: ActionableQuestionService,
    @Qualifier("emailTemplateRepository") private val gcsStreamRepository: GcsStreamRepository,
) {
    @Transactional
    fun loadContent(storyName: String): GameDataFile {
        require(storyName.isNotBlank()) { "storyName cannot be blank" }
        val blobId = gcsStreamRepository.getBlobIdStory(storyName, "loadContent.json")
        val blob = gcsStreamRepository.download(blobId)
        val mapper = ObjectMapper()

        val json = String(blob.getContent())
        val gameData = mapper.readValue(json, GameDataFile::class.java)

        val fixedBotMessagesRequest =
            FixedBotMessagesRequest(
                messages = gameData.fixedBotMessages,
            )

        actionableQuestionService.addLevelUpQuestion(gameData.levelUpQuestions, storyName)
        actionableQuestionService.addProtectedQuestion(gameData.protectedQuestions, storyName)
        fixedBotMessageService.addFixedBotMessages(fixedBotMessagesRequest, storyName, TRUE)

        return gameData
    }
}
