package code.nebula.cipherquest.service

import code.nebula.cipherquest.configuration.properties.CloudStorageProperties
import code.nebula.cipherquest.models.dto.GameDataFile
import code.nebula.cipherquest.models.requests.FixedBotMessagesRequest
import code.nebula.cipherquest.repository.LevelUpQuestionRepository
import code.nebula.cipherquest.repository.ProtectedQuestionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.cloud.storage.Storage
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class GCloudService(
    private val storage: Storage,
    private val cloudStorageProperties: CloudStorageProperties,
    private val fixedBotMessageService: FixedBotMessageService,
    private val levelUpQuestionRepository: LevelUpQuestionRepository,
    private val protectedQuestionRepository: ProtectedQuestionRepository,
) {
    @Transactional
    fun loadContent(storyName: String): GameDataFile {
        require(storyName.isNotBlank()) { "storyName cannot be blank" }
        val blob =
            storage[
                cloudStorageProperties.storiesBucket.name,
                "${cloudStorageProperties.storiesBucket.folder}/$storyName/loadContent.json",
            ] ?: throw IllegalArgumentException("File not found in bucket ${cloudStorageProperties.storiesBucket.name}")

        val mapper = ObjectMapper()

        val json = String(blob.getContent())
        val gameData = mapper.readValue(json, GameDataFile::class.java)

        val fixedBotMessages =
            FixedBotMessagesRequest(
                messages =
                    gameData.fixedBotMessages
                        .map { FixedBotMessagesRequest.FixedBotMessageRequest(type = it.type, content = it.content) },
            )

        levelUpQuestionRepository.save(gameData.levelUpQuestions, storyName)
        protectedQuestionRepository.save(gameData.protectedQuestions, storyName)
        fixedBotMessageService.addFixedBotMessages(fixedBotMessages, storyName)

        return gameData
    }
}
